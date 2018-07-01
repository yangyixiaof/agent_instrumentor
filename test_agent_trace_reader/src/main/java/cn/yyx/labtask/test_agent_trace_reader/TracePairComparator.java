package cn.yyx.labtask.test_agent_trace_reader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cn.yyx.labtask.runtime.memory.state.BranchNodesState;

/**
 * 比较两个 Trace——这两个 trace 应是一个 sequence 变异前和变异后的版本分别执行产生的——产生 reward。
 *
 * <p>
 * 可以产生
 */
public class TracePairComparator {

	private BranchNodesState branch_state = new BranchNodesState();

	/**
	 *
	 * @param previous_branch_signature
	 * @param current_branch_signature
	 * @return
	 */
	public Map<String, Double> BuildGuidedModel(Map<String, LinkedList<ValuesOfBranch>> previous_branch_signature,
			Map<String, LinkedList<ValuesOfBranch>> current_branch_signature) {
		Map<String, Double> influence = new TreeMap<>();
		Set<String> pset = previous_branch_signature.keySet();
		for (String sig : pset) {
			if (branch_state.BranchHasBeenIteratedOver(sig)) {
				continue;
			}
			double sig_influence = 0.0;
			LinkedList<ValuesOfBranch> previous_vobs = previous_branch_signature.get(sig);
			LinkedList<ValuesOfBranch> current_vobs = current_branch_signature.get(sig);
			if (previous_vobs == null) {
				previous_vobs = new LinkedList<ValuesOfBranch>();
			}
			if (current_vobs == null) {
				current_vobs = new LinkedList<ValuesOfBranch>();
			} // if (current_vob != null)
			Integer state = branch_state.GetBranchState(sig);
			Integer previous_state = IdentifyBranchState(previous_vobs);
			if (previous_state != null) {
				if (state == null) {
					state = previous_state;
				} else {
					state &= previous_state;
				}
			}
			Integer current_state = IdentifyBranchState(current_vobs);
			if (current_state != null) {
				if (state == null) {
					state = current_state;
				} else {
					state &= current_state;
				}
			}
			branch_state.PutBranchState(sig, state);
			if (previous_vobs.size() == 0) {
				if (current_vobs.size() > 0) {
					sig_influence = 1.0;
				} else {
					sig_influence = 0.0;
				}
			} else {
				if (current_vobs.size() == 0) {
					sig_influence = -1.0;
				} else {
					double min = Double.MAX_VALUE;
					Iterator<ValuesOfBranch> pitr = previous_vobs.iterator();
					while (pitr.hasNext()) {
						double max = Double.MIN_VALUE;
						ValuesOfBranch p_vob = pitr.next();
						Iterator<ValuesOfBranch> citr = current_vobs.iterator();
						while (citr.hasNext()) {
							ValuesOfBranch c_vob = citr.next();
							double influ = ComputeInfluenceForBranch(sig, state, p_vob, c_vob);
							if (max < influ) {
								max = influ;
							}
						}
						if (min > max) {
							min = max;
						}
					}
					sig_influence = min;
				}
			}
			influence.put(sig, sig_influence);
		} // for (String sig : pset)
		return influence;
	}

	private Integer IdentifyBranchState(LinkedList<ValuesOfBranch> vobs) {
		Integer state = null;
		Iterator<ValuesOfBranch> vob_itr = vobs.iterator();
		while (vob_itr.hasNext()) {
			ValuesOfBranch vob = vob_itr.next();
			double v1 = vob.GetBranchValue1();
			double v2 = vob.GetBranchValue2();
			switch (vob.GetCmpOptr()) {
			// ``compare then store'' series
			case "D$CMPG":
			case "D$CMPL":
			case "F$CMPG":
			case "F$CMPL":
			case "L$CMP": {
				if (state == null) {
					state = 0b111;
				}
				if (v1 == v2) {
					state &= 0b101;
				} else {
					if (v1 > v2) {
						state &= 0b110;
					} else {
						state &= 0b011;
					}
				}
			} // // ``compare then store'' series BLOCK
				break;
			// eq neq series... *8
			case "I$==":
			case "I$!=":
			case "A$==":
			case "A$!=":
			case "IZ$==":
			case "IZ$!=":
			case "N$!=":
			case "N$==": {
				if (state == null) {
					state = 0b11;
				}
				if (v1 == v2) {
					state &= 0b01;
				} else {
					state &= 0b10;
				}
			}
				break;
			// ge, ge 0 *2
			case "I$>=":
			case "IZ$>=": {
				if (state == null) {
					state = 0b11;
				}
				if (v1 >= v2) {
					state &= 0b10;
				} else {
					state &= 0b01;
				}
			}
				break;
			// le, le 0
			case "I$<=":
			case "IZ$<=": {
				{
					if (state == null) {
						state = 0b11;
					}
					if (v1 <= v2) {
						state &= 0b01;
					} else {
						state &= 0b10;
					}
				}
			}
				break;
			// gt, gt 0
			case "I$>":
			case "IZ$>": {
				if (state == null) {
					state = 0b11;
				}
				if (v1 > v2) {
					state &= 0b10;
				} else {
					state &= 0b01;
				}
			}
				break;
			// lt, lt 0
			case "I$<":
			case "IZ$<": {
				if (state == null) {
					state = 0b11;
				}
				if (v1 < v2) {
					state &= 0b01;
				} else {
					state &= 0b10;
				}
			}
				break;
			} // switch (current_vob.GetCmpOptr())
		}
		return state;
	}

	private double ComputeInfluenceForBranch(String sig, int state, ValuesOfBranch previous_vob, ValuesOfBranch current_vob) {
		double influence = 0.0;
		double prev_v1 = previous_vob.GetBranchValue1();
		double prev_v2 = previous_vob.GetBranchValue2();
		double v1 = current_vob.GetBranchValue1();
		double v2 = current_vob.GetBranchValue2();
		switch (current_vob.GetCmpOptr()) {
		// ``compare then store'' series
		case "D$CMPG":
		case "D$CMPL":
		case "F$CMPG":
		case "F$CMPL":
		case "L$CMP": {
			int state_copy = state;
			int position = 1;
			while (state_copy > 0) {
				int bit = state_copy & 0b1;
				if (bit == 1) {
					switch (position) {
					case 1: {
						double gap = (v1 - v2) - (prev_v1 - prev_v2);
						if (gap > 0) {
							influence = 1.;
						}
					}
						break;
					case 2: {
						double gap = Math.abs(v1 - v2);
						double prev_gap = Math.abs(prev_v1 - prev_v2);
						if (prev_gap - gap > 0) {
							influence = 1.;
						}
					}
						break;
					case 3: {
						double gap = (v1 - v2) - (prev_v1 - prev_v2);
						if (gap < 0) {
							influence = 1.;
						}
					}
						break;
					}
				}
				state_copy >>= 1;
				position++;
			}
		} // // ``compare then store'' series BLOCK
			break;
		// eq neq series... *8
		case "I$==":
		case "I$!=":
		case "A$==":
		case "A$!=":
		case "IZ$==":
		case "IZ$!=":
		case "N$!=":
		case "N$==": {
			int state_copy = state;
			int position = 1;
			while (state_copy > 0) {
				int bit = state_copy & 0b1;
				if (bit == 1) {
					switch (position) {
					case 1: {
						double gap = v1 - v2;
						if (gap != 0) {
							influence = 1.;
						}
					}
						break;
					case 2: {
						double gap = Math.abs(v1 - v2);
						double prev_gap = Math.abs(prev_v1 - prev_v2);
						if (prev_gap - gap > 0) {
							influence = 1.;
						}
					}
						break;
					}
				}
				state_copy >>= 1;
				position++;
			}
		}
			break;
		// ge, ge 0 *2
		case "I$>=":
		case "IZ$>=": {
			int state_copy = state;
			int position = 1;
			while (state_copy > 0) {
				int bit = state_copy & 0b1;
				if (bit == 1) {
					switch (position) {
					case 1: {
						double gap = v1 - v2;
						double prev_gap = prev_v1 - prev_v2;
						if (prev_gap - gap < 0) {
							influence = 1.;
						}
					}
						break;
					case 2: {
						double gap = v1 - v2;
						double prev_gap = prev_v1 - prev_v2;
						if (prev_gap - gap > 0) {
							influence = 1.;
						}
					}
						break;
					}
				}
				state_copy >>= 1;
				position++;
			}
		}
			break;
		// le, le 0
		case "I$<=":
		case "IZ$<=": {
			{
				int state_copy = state;
				int position = 1;
				while (state_copy > 0) {
					int bit = state_copy & 0b1;
					if (bit == 1) {
						switch (position) {
						case 1: {
							double gap = v1 - v2;
							double prev_gap = prev_v1 - prev_v2;
							if (prev_gap - gap < 0) {
								influence = 1.;
							}
						}
							break;
						case 2: {
							double gap = v1 - v2;
							double prev_gap = prev_v1 - prev_v2;
							if (prev_gap - gap > 0) {
								influence = 1.;
							}
						}
							break;
						}
					}
					state_copy >>= 1;
					position++;
				}
			}
		}
			break;
		// gt, gt 0
		case "I$>":
		case "IZ$>": {
			int state_copy = state;
			int position = 1;
			while (state_copy > 0) {
				int bit = state_copy & 0b1;
				if (bit == 1) {
					switch (position) {
					case 1: {
						double gap = v1 - v2;
						double prev_gap = prev_v1 - prev_v2;
						if (prev_gap - gap < 0) {
							influence = 1.;
						}
					}
						break;
					case 2: {
						double gap = v1 - v2;
						double prev_gap = prev_v1 - prev_v2;
						if (prev_gap - gap > 0) {
							influence = 1.;
						}
					}
						break;
					}
				}
				state_copy >>= 1;
				position++;
			}
		}
			break;
		// lt, lt 0
		case "I$<":
		case "IZ$<": {
			int state_copy = state;
			int position = 1;
			while (state_copy > 0) {
				int bit = state_copy & 0b1;
				if (bit == 1) {
					switch (position) {
					case 1: {
						double gap = v1 - v2;
						double prev_gap = prev_v1 - prev_v2;
						if (prev_gap - gap < 0) {
							influence = 1.;
						}
					}
						break;
					case 2: {
						double gap = v1 - v2;
						double prev_gap = prev_v1 - prev_v2;
						if (prev_gap - gap > 0) {
							influence = 1.;
						}
					}
						break;
					}
				}
				state_copy >>= 1;
				position++;
			}
		}
			break;
		} // switch (current_vob.GetCmpOptr())
		return influence;
	}

}
