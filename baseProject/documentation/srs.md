Software Requirement Specification
==================================

1. Introduction
------------
### 1.1 Purpose
This SRS is for the use of the developers and the customer of the Flatfinder application. The content declares the specification of the project which have to be uphold by the customer and the developers.
### 1.2 Stakeholders
This Software Project will be developed for the ESE/SCG group of the University of Bern. They will have all the rights of the the code and the publishing.
### 1.3 Definitions
Ad: Advertisement
SW: Software
### 1.4  Overview
Chapter 2 displays the customer specifications and wishes important for this project.
Chapter 3 is targeted to the developers and shows the specific requirements.
### 1.5 References
This specification references to the follwing link where all the tasks are listed: 
[Link to the Project description page on github](https://github.com/scg-unibe-ch/ese2016/wiki/Project-Description "Github ESE Project Description Homepage")
## 2. Overall description
-------------------
### 2.1 Use cases
The following section describes the behaviour of the FlatFindr Application. This should provide a high-level overview of the use cases and give an impression about its main conceptual ideas. The use cases are grouped as follows: 
1. User Management
2. Publish Ads
3. Advertisements in General (open, display, bookmarks etc.)
4. Searching Ads
5. Alerts
6. Visiting times
7. Messaging with Advertiser

#### 2.1.2 User Management
The following section provides an overview of the basic behavior of becoming a member of FlatFindr.

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
|1    | Users without account can sign up  | **Condition to show**<ul><li>User is not yet a member / there is no existing account </li><li>User has selected 'Login' in menu bar on home screen and afterwards selected 'Sign Up' on login page </li></ul><br>**Expected**<br>Registration page opens where user can enter his personal information to create an account. Part of the required informations are first name and last name, which will be displayed publically later on, e-mail address (username), password and gender. After successful creation of the account, user is back on login page and can login using his created credentials (email / password).<br><br>**Main Actor**<br>User with no existing account.                                     | HIGH |
| 2   | Login shown on start page         |**Condition to Show**<ul><li>User is not logged in</li><li>User is on start page of FlatFindr</li></ul><br>**Expected**<br>Login label is shown in the menu bar in upper right hand corner. When customer selects the login label, the login page is shown, where he can login.<br><br>**Main Actor** Member (not logged in) and User who wants to sign up.                                     | MED |
|3    | Login with email and password     |**Condition to show**<ul><li> user has an existing account at Flatfindr </li></ul><br>**Expected**<br>On login page, the user can enter his e-mail address and password, that was initially used to create the member account at FlatFinder to login.<br><br>**Main Actor**<br>User with existing Member account                                                                               | HIGH |
|4    | Logged in users can logout        |**Condition to show**<ul><li>User is logged in</li><li></li>user select menu in menubar (upper right-hand corner)</ul><br>**Expected**<br>User can select logout in the menu. Afterwards, he is logged out of the application i.e. does not have member privileges anymore.<br><br>**Main Actor**<br>User with existing member account                                  | MED |
|5    | Members can edit the profile      |**Condition to show**<ul><li>Member (existing account) </li><li>user navigates to public profile page</li></ul><br>**Expected**<br>Members can change the personal information provided during sign up by selecting "Edit profile". All personal information such as name, e-mail and password can be changed. Futhermore, the user can add a personal description in the 'About me' entry field. <br><br>**Main Actor**<br>User with existing Member Account                                                                                     | LOW |
|6   | Members can access public profiles |**Condition to show**<ul><li>Member (existing account) </li><li> Member opens the add of another member and selects 'Visit profile'</li></ul><br>**Expected**<br>Member can see the public profile of the other member. Visible information are the username (e-mail address), the name and the personal description (About me).<br><br>**Main Actor**<br>User with existing Member Account                                                                                      | LOW |

#### 2.1.2 Publish Flat Ads
The following section provides a high-level overview of how to publish a flat ad in the FlatFinder application. 

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
|7    | Members can publish ads            | **Condition to show**<ul><li> Member (existing account) </li><li> Logged in </li><li> Selecting menubar 'Publish Ads'</li></ul><br>**Expected**<br>A member can publish flat ads. In the publishing form, the user has to fill in general info such as Title of the ad, location of the flat, size, dates (for moving in and out again) and the price. Those information are required in order to publish the add. Furthermore, the user can select different characteristics that match the advertising flat e.g. wifi available. There is an open text field where the user/advertiser has to describe the room/flat further into detail. In the following, there are four optional sections to the publishing form.<ol><li>Roomates (see ID 7)</li><li>Preferences in a new inhabitant (see ID 8)</li><li>Pictures of the flat (see ID 9)</li><li>visiting times (see ID 10)</li></ol><br><br>**Main Actor**<br>User with existing member account i.e. Advertiser                                                 | HIGH |
|7   | Add roomates to ads                | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can link the add to room mates that also have an account on Flatfindr. This allows the interested person to check out the roomates as well. <br><br>**Main Actor**<br>User with exsting member account i.e. Advertiser and Roomates        | LOW |
|8   | Add Preferences to ads                | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can add an open text about their preferences in a new room mate, e.g. 'Student of computer science' or 'Must me male'.  <br><br>**Main Actor**<br>User with exsting member account i.e. Advertise| LOW |
|9   | Add Pictures to ads                | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can add pictures of the flat/room to the add. The advertiser can upload pictures from his local computer. After uploading, the form presents the picutres as tables indicating their size and name. By selecting 'Delete' the uploaded picutre can be removed again. There is currently no limit in the amount of files to be uploaded <br><br>**Main Actor**<br>User with exsting member account i.e. Advertise       | LOW |
|10   | Add Visiting times to ads         | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Publish ad form opened</li></ul><br>**Expected**<br>Optionally, advertisers can add possible visiting times to the add. The advertiser can first choose a day from the calender and then define a time period (hh:mm) when the visiting of the flat is possible. The advertiser can add as many visiting time periods as wished. <br><br>**Main Actor**<br>User with exsting member account i.e. Advertise       | LOW |
|11   | Edit ads                          | **Condition to show**<ul><li> Member </li><li> Logged in </li><li> Member has a published ad</li></ul><br>**Expected**<br>The advertiser can edit the information provided during ad publishing afterwards by selecting 'Edit ad' on the page of the corresponding advertisement. <br><br>**Main Actor**<br>User with exsting member account i.e. Advertise                                           | LOW |

#### 2.1.3 Advertisements in General (open, display, bookmarks etc.)
The following section provides an overview of the handling of ads once they are published i.e. the displaying, different tools to manage the ads etc. 

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| 12  | Latest ads displayed on home section | **Condition to show**<ul><li> FlatFinder opened on homescreen </li><li> at least one ad published</li></ul><br>**Expected**<br>The latest four ads are published directly on the landing page of Flatfindr, with the latest up on the top. They show the title of the add, type of flat, location and price.  <br><br>**Main Actor**<br>Flatfindr Application                              | MED |
| 13   | Open ads from landing page         | **Condition to show**<ul><li> at least one ad on landing page </li><li> user selecting the title of the ad</li></ul><br>**Expected**<br>The flat page opens showing all the details and information provided by the advertiser during publishing of the ad.  <br><br>**Main Actor**<br>User (either with or without account)                             | HIGH |
| 14 | Show advertiser on ad page           | **Condition to show**<ul><li> opened ad page</li></ul><br>**Expected**<br>On the bottom of the ad page, the advertiser himself is shown. Logged in users can visit the profile of the advertiser and contact him. Non-logged in user can only see the username.   <br><br>**Main Actor**<br>Users (with exsting account or without                                                                   | LOW |
| 15 | Members can send enquiries to visit  | **Condition to show**<ul><li> opened ad page</li><li>Member</li><li>Logged in</li></ul><br>**Expected**<br>In the section visiting times of the ad page, the logged in user can choose a date for his visit and select it to send the advertiser an equiry.<br><br>**Main Actor**<br>Users with existing Member account (logged in)                                                                  | LOW |
| 16 | Members can contact advertiser       | **Condition to show**<ul><li> opened ad page</li><li>Member</li><li>Logged in</li></ul><br>**Expected**<br>In the section 'Advertiser' of the ad page, the logged in user can choose whether to visit the advertiser's profile or to contact the advertiser.<br><br>**Main Actor**<br>Users with existing Member account (logged in)                                          | LOW |
| 17 | Bookmark ads                         | **Condition to show**<ul><li> opened ad page</li><li>Member</li><li>Logged in</li><li>Visitor is not the Advertiser of the ad</li></ul><br>**Expected**<br>On top of the ad page next to the tile, the user can select bookmark to remeber the ad. The ad will then be display in the menu 'My Rooms' in the section 'My bookmarks'<br><br>**Main Actor**<br>Users with existing Member account (logged in)                                                                 | LOW |
| 18 | Remove Bookmarks                       | **Condition to show**<ul><li> opened ad page</li><li>Member</li><li>Logged in</li><li>Visitor is not the Advertiser of the ad</li><li>Bookmarked ad</li></ul><br>**Expected**<br>User can remove bookmark at the same location where he bookmarked it (see ID 17). <br><br>**Main Actor**<br>Users with existing Member account and bookmarks (logged in)                                    | LOW |
| 19 | Own ads listed in 'My Rooms'        | **Condition to show**<ul><li>Member</li><li>Logged in</li><li>Published at least one ad</li></ul><br>**Expected**<br>All the published ads of a member are shown in the menu 'My Rooms' under the section 'My Advertisements'. <br><br>**Main Actor**<br>Advertisers                                                                               | LOW |

#### 2.1.4 Search Ads
The following section provides a high-level overview of the different search functionalities provided to find the right flat. 

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| 20 | Searching for a flat/ad             | **Condition to show**<ul><li> User selects 'Search' label in menu bar </li></ul><br>**Expected**<br>Search page opens where user can set different search criterias. First of all, he can choose between looking for a room or a studio. Other search criterias include general location and radius around this location and the max. price. After selecting search, the matches are shown in a list view. Selecting the ad title will open the advertisement. <br><br>**Main Actor**<br>User searching for a flat                                                           | HIGH |
| 21 | Filter the results                 | **Condition to show**<ul><li> User has searched for a flat </li></ul><br>**Expected**<br>Next to the list view of the search results, there is a tool for filtering the results. Firstly, it displays the initial search criterias, so the search can be detailed even further more without going back to the search view. Below, the user can add additional search criteria such as move in / move out date, flat charactersistics such as wifi, smokers etc. After ticking/adding the filters and selecting the filter button, the search results are reduced to the once matching the new filter criterias.  <br><br>**Main Actor**<br>User searching for a flat                                                                                  | HIGH |
| 22 | Sorting the results                 | **Condition to show**<ul><li> User has searched for a flat </li></ul><br>**Expected**<br>Above the list view of the search results there is a drop-down where the user can add sorting criteria. User should be able to sort according to price, move in date and date of ad creation (all of them ascending and descending). <br><br>**Main Actor**<br>User searching for a flat                                                                                                    | MED |

#### 2.1.5 Alerts
Alerts are a kind of automatic search. The following section explains the main use cases of this feature. 

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| 23 | Create alerts                       | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Opened Alerts section in menu</li></ul><br>**Expected**<br>User can create an alert by setting up the criteria for matching new flats/ads (similar to search criterias). If a new ad is published matching the search criteria, an alert will be send to the user. <br><br>**Main Actor**<br>User with existing member account.                                                   | MED |
| 24 | List all active alerts             | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Opened Alerts section in menu</li></ul><br>**Expected**<br>Below the alerts creation section, all active alerts are listed and can be managed i.e. deleted.  <br><br>**Main Actor**<br>User with existing member account.                                                                 | MED |

#### 2.1.6 Visiting times
Members can manage and arange visits with interested renters. The following section shall point out the main uses cases behind

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| 25 | Send enquiry for visiting           | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Ad with visiting times set up</li></ul><br>**Expected**<br>If the advertiser has setup visiting times (see ID 10) in his ad, then logged in users can send an enquiry for a certain (or as many as he wished) time period. The enquiry will be sent to the advertiser for acceptance. After the enquiry for a specific visiting time has been sent, the label changes to 'Enquiry sent' <br><br>**Main Actor**<br>User with existing member account                   | MED |
| 26 | Manage Enquiries for visiting        | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Ad with visiting times set up</li><li>user sent enquiry to visit at certain time</li></ul><br>**Expected**<br>All enquiries sent by interested users to visit the flat at a certain visiting time are shown listed in the menu under 'Enquiries'. The Advertiser can then see the sender of the enquiry (username), the affected ad, selected visiting time and the date the enquiry was sent. The advertiser then can either accept or decline the enquire for the visit. The selection can be undone until the page is refreshed. Afterwards, the decision is shown in the table with the visits and the user is informed corespondingly. Accepted visitors are added to the scheduler (see ID 27) <br><br>**Main Actor**<br>User with existing member account i.e. Advertiser  | HIGH |
| 27 | List advertisers presentations      | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Visiting times set up (ID 10) </li></ul><br>**Expected**<br>Each visiting time that was setup during publishing of the ad, there is a line in the scheduler (Menu point). For each visit entry in the table there is the option to see the ad and the list of  visitors (see ID 29)for this specific visit. <br><br>**Main Actor**<br>User with existing member account i.e. Advertiser      | MED |
| 28 | List own visits                    | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Sent enquiry for an ad/visiting time </li><li>Enquiry accepted by Advertiser</li></ul><br>**Expected**<br>All accepted enquiries for visiting rooms/flats are shown in the scheduler in the 'Your visits' section. They show the address of each flat, the arranged date and time of the visit and a shortcut to the ad page. <br><br>**Main Actor**<br>User with existing member account i.e. Advertiser      | MED |
| 29 | Show visitors                    | **Condition to show**<ul><li>         Member</li><li>Logged In</li><li>Sent enquiry for an ad/visiting time </li><li>Enquiry accepted by Advertiser</li></ul><br>**Expected**<br>For each visit in the scheduler (see ID 26), the advertiser can list the visitors. The visitors are shown in a tbale indicating their Full name, username, shortcut to their profile and a 5-Star rating system. In the rating system, the advertiser can rank his visitors and choose the best suiting.  <br><br>**Main Actor**<br>User with existing member account i.e. Advertiser     | MED |

#### 2.1.7 Messaging with Advertiser
In order to better arrange visits and clarfiy questions about flats, there is the possibilty to contact an advertiser. The main use cases in this respect are listed below.

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| 30 | Contact Advertiser                  | **Condition to show**<ul><li>         Member</li><li>Logged In</li><li>Opened ad page of flat</li></ul><br>**Expected**<br>In the advertiser section of each ad, logged in users can choose to contact the advertiser. If selected a pop up will open where the user has to type in the subject of his message and the message itself. The advertiser can see the message in the 'Messages' section in the menu (see ID 31). <br><br>**Main Actor**<br>User with existing member account | MED |
| 31 | Receive and open Messages           | **Condition to show**<ul><li>         Member</li><li>Logged In</li><li>Advertiser of a flat</li><li>user sent message</li></ul><br>**Expected**<br>In the messaging section, new messages are shown in the inbox, indicating the subject, sender, recipient and date. If one message is selected, a preview of the message is shown below. Sent messages can be viewed similarly in the 'Sent' Section. <br><br>**Main Actor**<br>Advertiser                             | MED |
| 32 | Advertisers new Messages           | **Condition to show**<ul><li>         Member</li><li>Logged In</li><li>Advertiser</li><li>Messaging section in menu</li></ul><br>**Expected**<br>Similarly to ID 30, the advertiser can select 'New' in the messaging section. A pop up will open where he can type in the username of a user (if not exsting there will be an error), subject and message.  <br><br>**Main Actor**<br>Advertiser                                                            | MED |
| 32 | New message indicator             | **Condition to show**<ul><li>         Member</li><li>Logged In</li><li>Unread message received</li></ul><br>**Expected**<br>If the user receives a new message, the 'Messaging' menu point shows the number of unread messages in brackets as a suffix, e.g. One unread Message would be 'Messages (1)'. After reading the message, the brackets disappear.  <br><br>**Main Actor**<br>User with existing account                                                                         | LOW |


Sample table for extension. 

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| newID | newTitle| **Condition to show**<ul><li> item1 </li><li> item 2 </li><li> item 3 </li></ul><br>**Expected**<br>TEXT <br><br>**Main Actor**<br>TEXT | PRIO |


3. Specific requirements
---------------------
### 3. 1 Functional requirements
### 3.2 Non-functional requirements (external, performance, etc.)
