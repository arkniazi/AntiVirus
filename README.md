# AntiVirus
Cloud based Android Antivirus(also used permission based malware detection)
This application is developed for android to help identify malwares.

Techniques used for malware detection
1.  Signature based detection.
  Verify the signature of application. I compared the application signature(alreaady generated) with the signature I have generated.
  If it doesn't match then it will display warning.
2.  Checking dex code hash with the malware database.
  I have created a database for malware's dex hashes. I send the application hash to my server where I compare the it with malware
  database. 
3.  AI based malware detection(check on the basis of permission).
    This method is not perfect. Because I was using the permission based SVM model to verify application integrity.
    Which is not that efficient way. I gathered data for svm form malwares and non-malware's application. 
    
Here is the link for server side scripts.
https://github.com/arkniazi/serverscripts 

To compile this application you might need to resolve some dependecy issues.
