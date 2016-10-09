Software Requirement Specification
==================================

Introduction
------------
### Purpose
### Stakeholders
### Definitions
### System overview
### References

## 2. Overall description
-------------------
### 2.1 Use cases
The following section describes the behaviour of the FlatFindr Application. This should provide a high-level overview of the use cases and give an impression abou the main conceptual ideas behind. The use cases are grouped as follows: 
1. User Management
2. Publish ads 


#### 2.1.2 User Management
The following section provides an overview of the basic behavior of becoming a member of FlatFindr. 
| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
|1    | Users without account can sign up  | **Condition to show**<ul><li>User is not yet a member / there is no existing account </li><li> user has selected 'Login' in menu bar on home screen and afterwards selected 'Sign Up' on login page </li></ul><br>**Expected**<br>Registration page opens where user can enter his personal information to create an account. Part of the required informations are first name and last name, which will be displayed publically later on, e-mail address (username), password and gender. After successful creation of the account, user is back on login page and can login using his created credentials (email / password).<br><br>**Main Actor**<br>User with no existing account.                                     | HIGH |
| 2   | Login shown on start page         |**Condition to Show**<ul><li>User is not logged in</li><li>User is on start page of FlatFindr</li></ul><br>**Expected**<br>Login label is shown in the menu bar in upper right hand corner. When customer selects the login label, the login page is shown, where he can login.<br><br>**Main Actor** Member (not logged in) and User who wants to sign up.                                     | MED |
|3    | Login with email and password     |**Condition to show**<ul><li> user has an existing account at Flatfindr </li></ul><br>**Expected**<br>On login page, the user can enter his e-mail address and password, that was initially used to create the member account at FlatFinder to login.<br><br>**Main Actor**<br>User with existing Member account                                                                               | HIGH |
|4    | Logged in users can logout        |**Condition to show**<ul><li>User is logged in</li><li></li>user select menu in menubar (upper right-hand corner)</ul><br>**Expected**<br>User can select logout in the menu. Afterwards, he is logged out of the application i.e. does not have member privileges anymore.<br><br>**Main Actor**<br>User with existing member account                                  | MED |
|5    | Members can edit the profile      |**Condition to show**<ul><li>Member (existing account) </li><li>user navigates to public profile page</li></ul><br>**Expected**<br>Members can change the personal information provided during sign up by selecting "Edit profile". All personal information such as name, e-mail and password can be changed. Futhermore, the user can add a personal description in the 'About me' entry field. <br><br>**Main Actor**<br>User with existing Member Account                                                                                     | LOW |
|6   | Members can access public profiles |**Condition to show**<ul><li>Member (existing account) </li><li> Member opens the add of another member and selects 'Visit profile'</li></ul><br>**Expected**<br>Member can see the public profile of the other member. Visible information are the username (e-mail address), the name and the personal description (About me).<br><br>**Main Actor**<br>User with existing Member Account                                                                                      | LOW |


#### 2.1.2 Publish Flat Ads
The following section provides a high-level overview of how to publish a flat ad in the FlatFinder application. 
| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
|7    | Members can publish ads            | **Condition to show**<ul><li> Member (existing account) </li><li> Logged in </li><li> Selecting menubar 'Publish Ads'</li></ul><br>**Expected**<br>A member can publish flat ads. In the publishing form, the user has to fill in general info such as Title of the ad, location of the flat, size, dates (for moving in and out again) and the price. Those information are required in order to publish the add. Furthermore, the user can select different characteristics that match the advertising flat e.g. wifi available. There is an open text field where the user/advertiser has to describe the room/flat further into detail. In the following, there are four optional sections to the publishing form.<ol><li>Roomates (see ID 7)</li><li>Preferences in a new inhabitant (see ID 8)</li><li>Pictures of the flat (see ID 9)</li><li>visiting times (see ID 10)</li><ol><br><br>**Main Actor**<br>User with existing member account i.e. Advertiser                                                 | HIGH |
|7   | Add roomates to ads                | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can link the add to room mates that also have an account on Flatfindr. This allows the interested person to check out the roomates as well. <br><br>**Main Actor**<br>User with exsting member account i.e. Advertiser and Roomates        | LOW |
|8   | Add Preferences to ads                | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can add an open text about their preferences in a new room mate, e.g. 'Student of computer science' or 'Must me male'.  <br><br>**Main Actor**<br>User with exsting member account i.e. Advertise| LOW |
|9   | Add Pictures to ads                | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can add pictures of the flat/room to the add. The advertiser can upload pictures from his local computer. After uploading, the form presents the picutres as tables indicating their size and name. By selecting 'Delete' the uploaded picutre can be removed again. There is currently no limit in the amount of files to be uploaded <br><br>**Main Actor**<br>User with exsting member account i.e. Advertise       | LOW |
|10   | Add Visiting times to ads         | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can add possible visiting times to the add. The advertiser can first choose a day from the calender and then define a time period (hh:mm) when the visiting of the flat is possible. The advertiser can add as many visiting time periods as wished. <br><br>**Main Actor**<br>User with exsting member account i.e. Advertise       | LOW |





### Actor characteristics

Specific requirements
---------------------
### Functional requirements
### Non-functional requirements (external, performance, etc.)
