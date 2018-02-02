#/bin/bash

# instrument
python instrument_apk.py mobileqq_android.apk

# jarsigner
echo 12345678 | jarsigner -verbose -keystore yyx_apk.keystore -signedjar operation_pool/mobileqq_android_signed.apk operation_pool/mobileqq_android.apk yyx_apk.keystore

# run test
./run.sh operation_pool/mobileqq_android_signed.apk com.tencent.mobileqq activity.SplashActivity