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
LoggedInUser: Is a user that is logged in
NotLoggedInUser: Is a user that is not logged in
i: implemented
p: partially implemented
ni: not implemented yet
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
4. Searching and Displaying Ads
5. Alerts
6. Visitations
7. Messaging with Advertiser

#### 2.1.2 User Management
The following section provides an overview of the basic behavior of becoming a member of FlatFindr.

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
|1    | Users without account can sign up  | **Condition to show**<ul><li>User is not yet a member / there is no existing account </li><li>User has selected 'Login' in menu bar on home screen and afterwards selected 'Sign Up' on login page </li></ul><br>**Expected**<br>Registration page opens where user can enter his personal information to create an account. Part of the required informations are first name and last name, which will be displayed publically later on, e-mail address (username), password and gender. After successful creation of the account, user is back on login page and can login using his created credentials (email / password).<br><br>**Main Actor**<br>User with no existing account.                                     | HIGH |
| 2   | Login shown on start page         |**Condition to Show**<ul><li>User is not logged in</li><li>User is on start page of FlatFindr</li></ul><br>**Expected**<br>Login label is shown in the menu bar in upper right hand corner. When customer selects the login label, the login page is shown, where he can login.<br><br>**Main Actor** Member (not logged in) and User who wants to sign up.                                     | MED |
|3    | Login with email and password     |**Condition to show**<ul><li> user has an existing account at Flatfindr </li></ul><br>**Expected**<br>On login page, the user can enter his e-mail address and password, that was initially used to create the member account at FlatFinder to login.<br><br>**Main Actor**<br>User with existing Member account                                                                               | HIGH |
|4    | Logged in users can logout        |**Condition to show**<ul><li>User is logged in</li><li>user select menu in menubar (upper right-hand corner)</li></ul><br>**Expected**<br>User can select logout in the menu. Afterwards, he is logged out of the application i.e. does not have member privileges anymore.<br><br>**Main Actor**<br>User with existing member account                                  | MED |
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

#### 2.1.4 Search and Display Ads
The following section provides a high-level overview of the different search functionalities provided to find the right flat. 

| Title                              | Description                      | Priority |
|:-----------------------------------|:---------------------------------|:---------|
| Searching for a flat/ad             | **Expected**<br>Search page opens where user can set different search criterias (including the option to search for properties to be sold). <br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                     | i |
| Create an alert off a search         |**Expected**<br>A new alert can be created by clicking a button near the search criterias.<br><br>**Main Actor**<br>LoggedInUser                                                       | ni |
| Filter the results                 |**Expected**<br>Next to the list view of the search results, there is a tool for filtering the results with different criterias.<br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                                                  | i |
| Sorting the results                 |**Expected**<br>Above the list view of the search results there is a drop-down where the user can add sorting criteria.<br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                                                                    | i |
| Display an ad                       | **Expected**<br>Upon clicking on the ad, a detailed view containing all the information is being displayed to the user. <br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                                                                                                       | i |

#### 2.1.5 Alerts
Alerts are a kind of automatic search. The following section explains the main use cases of this feature. 

| ID  | Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| 23 | Create alerts                       | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Opened Alerts section in menu</li></ul><br>**Expected**<br>User can create an alert by setting up the criteria for matching new flats/ads (similar to search criterias). If a new ad is published matching the search criteria, an alert will be send to the user. <br><br>**Main Actor**<br>User with existing member account.                                                   | MED |
| 24 | List all active alerts             | **Condition to show**<ul><li> Member</li><li>Logged In</li><li>Opened Alerts section in menu</li></ul><br>**Expected**<br>Below the alerts creation section, all active alerts are listed and can be managed i.e. deleted.  <br><br>**Main Actor**<br>User with existing member account.                                                                 | MED |

#### 2.1.6 Visitations
Advertisers can manage and arange visits with interested renters. The following section shall point out the main use cases behind

| Title                              | Description                      | Status |
|:-----------------------------------|:---------------------------------|:---------|
| Send enquiry for visiting           | **Expected**<br>If the advertiser has setup visiting times in his ad, then logged in users can send an enquiry for a certain (or as many as he wished) time period. The advertiser can accept or decline the enquiries.<br><br>**Main Actor**<br>LoggedInUser                   | i |
| Manage Enquiries for visiting        | **Expected**<br>All enquiries sent by interested users to visit the flat at a certain visiting time are shown in a list and can be accepted and declined by the advertiser. A schedule will be displayed.<br><br>**Main Actor**<br>LoggedInUser  | i |
| List advertisers presentations      | **Expected**<br>Each visiting time that was setup during publishing of the ad will be displayed in a scheduler.<br><br>**Main Actor**<br>LoggedInUser      | i |
| List own visits                    | **Expected**<br>All accepted enquiries for visiting rooms/flats are shown in the scheduler in the 'Your visits' section. <br><br>**Main Actor**<br>LoggedInUser      | i |
| Show visitors                    | **Expected**<br>For each visit in the scheduler, the advertiser can list the visitors in a table and can use a rating system. <br><br>**Main Actor**<br>LoggedInUser     | i |
| Display overview list of most promising visitors           | **Expected**<br>A list of most promising visitors can be displayed for each room which is automatically generated based on the individual ratings. <br><br>**Main Actor**<br>LoggedInUser    | i |

#### 2.1.7 Messaging with Advertiser
In order to better arrange visits and clarfiy questions about flats, there is the possibilty to contact an advertiser. The main use cases in this respect are listed below.

| Title                              | Description                      | Status |
|:-----------------------------------|:---------------------------------|:---------|
| Contact Advertiser                  | **Expected**<br>In the advertiser section of each ad, logged in users can choose to contact the advertiser. If selected a pop up will open where the user has to type in the subject of his message and the message itself. <br><br>**Main Actor**<br>LoggedInUser | i |
| Receive and open Messages           | **Expected**<br>In the messaging section, new messages are shown in the inbox, indicating the subject, sender, recipient and date. If one message is selected, a preview of the message is shown below. Sent messages can be viewed similarly in the 'Sent' Section.<br><br>**Main Actor**<br>LoggedInUser                            | i |
| Advertisers new Messages           | **Expected**<br>Similarly to "Contact Advertiser", the advertiser can select 'New' in the messaging section. A pop up will open where he can type in the username of a user (if not exsting there will be an error), subject and message.  <br><br>**Main Actor**<br>Advertiser                                                            | i |
| New message indicator             | **Expected**<br>If the user receives a new message, the 'Messaging' menu point shows the number of unread messages in brackets as a suffix, e.g. One unread Message would be 'Messages (1)'. After reading the message, the brackets disappear.  <br><br>**Main Actor**<br>LoggedInUser                                                                        | i |

### 2.2 Actor Characteristics

* **Human actors**
	* **Member**: Everybody with an account qualifies as a member.
		* **Advertiser**: Use the site with the intention of renting out flats or sell properties. 
		* **Interest**: Are trying to find flats to rent or properties to buy.
		* **Operator**: Site administrator with options to manage users etc.
		* **Roommate** Only used because roommates can be defined in the description of flats.
	* **Non-Memberes**: Users without a login; they can still use search.
* **Computer**: This list is currently incomplete.
	* **Server**
	* **Mail Server**
	* **Push-notification server**

3. Specific requirements
---------------------
### 3. 1 Functional requirements
Distinction between use case and functional requirement not sufficiently clear. It seems that many use cases might as well be functional requirements (e.g. 'Show visitors' or 'List own visits'), thus creating redundancy.

Probable distinct requirements:
* New message indicator (from above)
* Distinction between regular and premium users.

#### 3.1.4 Search and Display Ads
The following section provides a high-level overview of the different search functionalities provided to find the right flat. 

| Title                              | Description                      | Priority |
|:-----------------------------------|:---------------------------------|:---------|
| Searching for a flat/ad             | **Expected**<br>Search page opens where user can set different search criterias (including the option to search for properties to be sold). First of all, he can choose between looking for a room or a studio. Other search criterias include general location and radius around this location and the max. price. After selecting search, the matches are shown in a list view. Selecting the ad title will open the advertisement. <br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                     | i |
| Create an alert off a search         |**Expected**<br>A new alert can be created by clicking a button. A success/failure-message is beings shown, while the user remains on the search page. The alert is added to the 'Create and manage alerts' page where it can be managed.<br><br>**Main Actor**<br>LoggedInUser                                                       | ni |
| Filter the results                 |**Expected**<br>Next to the list view of the search results, there is a tool for filtering the results. Firstly, it displays the initial search criterias, so the search can be detailed even further more without going back to the search view. Below, the user can add additional search criteria such as move in / move out date, flat charactersistics such as wifi, smokers etc. After ticking/adding the filters and selecting the filter button, the search results are reduced to the once matching the new filter criterias.  <br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                                                  | i |
| Sorting the results                 |**Expected**<br>Above the list view of the search results there is a drop-down where the user can add sorting criteria. User should be able to sort according to price, move in date and date of ad creation (all of them ascending and descending). <br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                                                                    | i |
| Display an ad                       | **Expected**<br>Upon clicking on the ad, a detailed view containing all the information is being displayed to the user. <br><br>**Main Actor**<br>NotLoggedInUser, LoggedInUser                                                                                                                                       | i |
#### 3.1.6 Visitations
Advertisers can manage and arange visits with interested renters. The following section shall point out the main use cases behind

| Title                              | Description                      | Status |
|:-----------------------------------|:---------------------------------|:---------|
| Send enquiry for visiting           | **Expected**<br>If the advertiser has setup visiting times in his ad, then logged in users can send an enquiry for a certain (or as many as he wished) time period. The enquiry will be sent to the advertiser for acceptance. After the enquiry for a specific visiting time has been sent, the label changes to 'Enquiry sent' <br><br>**Main Actor**<br>LoggedInUser                   | i |
| Manage Enquiries for visiting        | **Expected**<br>All enquiries sent by interested users to visit the flat at a certain visiting time are shown listed in the menu under 'Enquiries'. The Advertiser can then see the sender of the enquiry (username), the affected ad, selected visiting time and the date the enquiry was sent. The advertiser then can either accept or decline the enquire for the visit. The selection can be undone until the page is refreshed. Afterwards, the decision is shown in the table with the visits and the user is informed corespondingly. Accepted visitors are added to the scheduler <br><br>**Main Actor**<br>LoggedInUser  | i |
| List advertisers presentations      | **Expected**<br>Each visiting time that was setup during publishing of the ad, there is a line in the scheduler (Menu point). For each visit entry in the table there is the option to see the ad and the list of  visitors for this specific visit. <br><br>**Main Actor**<br>LoggedInUser      | i |
| List own visits                    | **Expected**<br>All accepted enquiries for visiting rooms/flats are shown in the scheduler in the 'Your visits' section. They show the address of each flat, the arranged date and time of the visit and a shortcut to the ad page. <br><br>**Main Actor**<br>LoggedInUser      | i |
| Show visitors                    | **Expected**<br>For each visit in the scheduler, the advertiser can list the visitors. The visitors are shown in a table indicating their full name, username, shortcut to their profile and a 5-Star rating system. In the rating system, the advertiser can rank his visitors and choose the best suiting.  <br><br>**Main Actor**<br>LoggedInUser     | i |
| Display overview list of most promising visitors           | **Expected**<br>A list of most promising visitors can be displayed for each room which is automatically generated based on the individual ratings. <br><br>**Main Actor**<br>LoggedInUser    | i |

### 3.2 Non-functional requirements (external, performance, etc.)

See questions below.

4. Questions
------------
Asked: 
* Ads for properties to be sold: What are differences in respect to their fields, compared to rentals? - *Just be reasonsable*
* Auctions: Which specific features / modes are to be supported (e.g. direct buy, ...)? - *Plain auction, nothing fancy*
* Closing: What happens if a deal is established, both, in reference to rentals and sells. - *Nothing, really... except probably a message to inform the parties*
* Extend search to include more filter critieria - which ones? - *the more important ones of those already in the filter that shows with the results*
* Should features for site admins be planned? Their capabilities would include unrestricted user, flat- and property management. - *Not at this stage*
* Alerts: When normal users get alerts "a bit late" - how much late? - *No clear answer, make it an option*

Not asked:
* Messaging: Should users be able to respond conveniently to messages?
* Can ads be removed again (particularly during auctions)?
* Can alerts be modified?
* Search results list: What does "a bit higher" for premium users entail? Should they be highlighted?
* Non-functional requirements:
	* Plattform for it to run on?
		* Backup solutions?
	* Internationalization requirements?
	* Specific security requirements?
	* Performance requirements
	* End devices