# Shake
Shake for exchanging contact





Introduce
===============================================================
Let me introduce our Shake project. 
At this Introduce part, we are going to introduce our motivation, purpose of our project, and core system functionality.

<img src="/app/src/main/res/drawable/page_num1.png" width="150" height ="200"> <img src="/app/src/main/res/drawable/page_num2.png" width="150" height ="200"> 
<img src="/app/src/main/res/drawable/page_num3.png" width="150" height ="200"> 
<img src="/app/src/main/res/drawable/page_num4.png" width="150" height ="200"> 
<img src="/app/src/main/res/drawable/page_num5.png" width="150" height ="200"> 
<img src="/app/src/main/res/drawable/page_num6.png" width="150" height ="200"> 
<img src="/app/src/main/res/drawable/page_num7.png" width="150" height ="200">
## Motivation
```
Everyone wants to be easy in every area of life. Based on these reasons, we have considered which parts of human behavior can be simpler. After thinking about it, we came to think about where people first met and formed relationships, and also thought about what action is necessary in the process. People are bound to exchange contacts at the first meeting, so we have started this project to make it simpler and more new.
```
## Link 
```
You can download our application below.
https://play.google.com/store/apps/details?id=org.androidtown.shaketest
```
## Our Porject
```
People always want simple action
Our application provide simple contact exchange service
```
## Key Feature
```
How to provide that Service
Shake , Contact , Save
```
### Shake 
Shake their device to ready for exchanging contacts
### Contact
Contact devices to exchange their contact data which contains name, e-mail, phone number, profile image
### Save
After exchanging each other, Save opponent data into user’s local mobile contact list and Server which implemented by Firebase Service

## User Requirements
< Functional >
User can exchange their contact with another user
The System provides multiple ways to exchange contacts
Determine what kind of data is contained in the profile such as name, phone number, and e-mail address
Users can hold other user’s profiles

< Non-Functional >
Device’s Battery issue caused by Service provided by System
If Service that run on background is always turn on, It can be causing battery issue.
The application has elements that can attract attention from people
Users need a profile they can decorate themselves such as business card
Users can keep their personal information from some people that user doesn’t want to share their information

## System Requirements
< Functional >
The system provides the service that user can transfer data to someone who doesn’t have our application
Users exchange their profile containing their information by using NFC
When users can’t use NFC function, users can use QR code containing their information instead of NFC. So, system provides the function to generate QR code
System needs check function to determine if they use the data service transfer service or not. The data transfer service is only used when check function is checked
When user receives another user’s profile, system can parse it and save data from received profile into user’s device automatically

< Non-functional >
System provides the service that user can decorate and edit their own profile
System provides multiple profile templates to users. So, they can decorate and edit their profile
In order to reduce the NFC transmission cost value, we will make the series of string data instead of sending the profile itself.



Implementation
===============================================================
## Shake
```
Using Accelerometer Sensor
BroadcastReceiver 
Service
SharedPreference
```
## Contact	

```
NFC(Near-Field-Communication)
QR Code
```
## Save Service
``` 
Automatically Save and Manage Contacts
Firebase – Realtime Database
ContentProvider
```
