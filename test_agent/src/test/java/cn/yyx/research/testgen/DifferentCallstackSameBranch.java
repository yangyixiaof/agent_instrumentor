package cn.yyx.research.testgen;

/**
 * 本测试用例旨在说明用 call stack 中的 method 序列（加上 relative offset）来标识分支结点是不合理的， 应当就用 enclosing method 加上
 * relative offset。
 */
public class DifferentCallstackSameBranch {
  private static void f(int n) {
    if (n > 1919) {

    } else {

    }
  }

  private static void g(Integer n) {
    f(n);
  }

  public static void main(String[] args) {
    int n = 2929;
    f(n);
    g(n);
  }
}

/*
产生 trace.txt 的主体（目前 2018-05-31 16:43:00 的格式）：

@Method-Enter:main~([Ljava/lang/String;)V#
@Method-Enter:f~(I)V#
@Branch-Operand:1:I$<=:#1919#2929#
@Method-Exit:f~(I)V#
@Method-Enter:g~(Ljava/lang/Integer;)V#
@Method-Enter:f~(I)V#
@Branch-Operand:1:I$<=:#1919#2929#
@Method-Exit:f~(I)V#
@Method-Exit:g~(Ljava/lang/Integer;)V#
@Method-Exit:main~([Ljava/lang/String;)V#

若用 method 序列加上 relative offset 标识，则代码静态意义上的一个分支结点会有多种表示（写成 JSON）：
{
	methods: ["main~([Ljava/lang/String;)V","f~(I)V"],
	offset: 1
}
{
	methods: ["main~([Ljava/lang/String;)V","g~(Ljava/lang/Integer;)V","f~(I)V"],
	offset: 1
}
为了不重不漏地标识分支结点，我们希望一个分支结点只有一种表示。
若用 enclosing method 加上 relative offset 标识，可满足此希望：
{
	methods: "f~(I)V",
	offset: 1
}

Ouch，还差类名。TODO

 */
