import os
import subprocess

iRet=subprocess.call('./gradlew :app:lintFull',shell = True)
if iRet:
   print("gradlew :app:lintFull success >>>>>>")





