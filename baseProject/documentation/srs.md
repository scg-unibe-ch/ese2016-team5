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

Status:
* I: Implemented
* PI: Partially Implemented
* NI: Not Implemented

Main actor
* LIU: Logged in user
* NLIU: Not logged in user

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

#### 2.1.1 User Management
The following section provides an overview of the basic behavior of becoming a member of FlatFindr.

| Title                              | Description                      | Priority |
|:--- |:-----------------------------------|:---------------------------------|:---------|
| Users without account can sign up  | **Trigger**<br>User clicked on 'Sign up' Link which is found in top right corner<br><br>**Expected**<br>User has selected 'Login' in menu bar on home screen and afterwards selected 'Sign Up' on login page. Registration page opens where user can enter his personal information to create an account.<br><br>**Main Actor**<br>NLIU                                     | HIGH |
| Log in     |**Trigger**<br>User clicked on 'Login'-button found in top right corner. <br><br>**Expected**<br>On login page, the user can enter his e-mail address and password. Expected behavior: User is logged in, user menu has changed.<br><br>**Main Actor**<br>NLIU                                                                               | HIGH |
| Logged in users can logout        |**Trigger**<br>User clicked on 'Log out'-button in top right corner.<br><br>**Expected**<br>User can select logout in the menu. Afterwards, he is logged out of the application i.e. does not have member privileges anymore.<br><br>**Main Actor**<br>LIU                                  | HIGH |
| Users can edit the profile      |**Trigger**<br>User opened the menu and lcicked on 'Public profile', then 'Edit Profile'.<br><br>**Expected**<br>User can change his personal information through a standard form.<br><br>**Main Actor**<br>LIU                                                                                     | LOW |
| Users can access public profiles |**Trigger**<br>In visitor list, user clicked on 'Visit' button.<br><br>**Expected**<br>Member can see the public profile of the other member. Visible information are the username (e-mail address), the name and the personal description (About me).<br><br>**Main Actor**<br>LIU                                                                                      | LOW |

#### 2.1.2 Publish Flat Ads
The following section provides a high-level overview of how to publish a flat ad in the FlatFinder application. 

| Title                              | Description                      | Priority |
|:-----------------------------------|:---------------------------------|:---------|
| Logged in users can publish ads            | **Trigger**<ul><li>'Publish Ads' in menu bar selected</li></ul>**Expected**<br>A member can publish flat ads. In the publishing form, the user has to fill in various information which are required to publish the ad.<br><br>**Sections**<br>General info<br><ul><li>Ad Title</li><li>Street</li><li>City / Zip code</li><li>Move-in date: Display calendar when clicking into field</li><li>Move-out date: Display calendar when clicking into field</li><li>Price per Month</li><li>Square meters</li><li>Type: Radio buttons [Room, Studio, Property]</li><li>Offer Type: Radio buttons [Rent, Auction]</li></ul>Room Description<ul><li>Characteristics: Checkboxes</li><li>Room Description: Textarea</li></ul>Roommates (Optional)<ul><li>Add by email: Textfield to search for existing accounts with a +-button to add them.</li><li>Additional roommates: Textarea for description</li></ul>Preferences (Optional)<ul><li>Preferences: Textarea</li></ul>Pictures (Optional)<ul><li>Picture: File upload field.</li><li>Picture overview: Grid showing name and size with option to delete image. Images are displayed immediately.</li></ul>Visiting times (Optional)<ul><li>Visiting times: Dates with calendar, to- and from times as text fields</li></ul><br>**Main Actor**<br>LIU                                                 | HIGH |
| Edit ads                          | **Trigger**<ul><li>'Edit ad' in 'My rooms' clicked</li></ul>**Condition to show**<ul><li> Member has a published ad</li></ul>**Expected**<br>The user can modify an ad. Functionality is similar to use case above. <br><br>**Main Actor**<br>LIU                                           | MID |

#### 2.1.3 Advertisements in General (open, display, bookmarks etc.)
The following section provides an overview of the handling of ads once they are published i.e. the displaying, different tools to manage the ads etc. 

| Title                              | Description                      | Status |
|:-----------------------------------|:---------------------------------|:---------|
| Latest ads displayed on home section | **Condition to show**<ul><li> FlatFinder opened on homescreen </li><li> at least one ad published</li></ul><br>**Expected**<br>The latest four ads are published directly on the landing page of Flatfindr, with the latest up on the top. They show the title of the add, type of flat, location and price.  <br><br>**Main Actor**<br>Flatfindr Application                              | i |
| Open ads from landing page         | **Condition to show**<ul><li> at least one ad on landing page </li><li> user selecting the title of the ad</li></ul><br>**Expected**<br>The flat page opens showing all the details and information provided by the advertiser during publishing of the ad.  <br><br>**Main Actor**<br>LoggedInUser or NonLoggedInUser                             | i |
| Show advertiser on ad page           | **Condition to show**<ul><li> opened ad page</li></ul><br>**Expected**<br>On the bottom of the ad page, the advertiser himself is shown. Logged in users can visit the profile of the advertiser and contact him. Non-logged in user can only see the username.   <br><br>**Main Actor**<br>LoggedInUser or NonLoggedInUser | i |
| Bookmark ads / Remove bookmarks                         | **Condition to show**<ul><li> opened ad page</li><li>Member</li><li>Logged in</li><li>Visitor is not the Advertiser of the ad</li></ul><br>**Expected**<br>On top of the ad page next to the tile, the user can select "Bookmark Ad" to remember the ad. The ad will then be display in the menu 'My Rooms' in the section 'My bookmarks.' By clicking again (where it now says "Bookmarked"), the bookmark is removed.<br><br>**Main Actor**<br>LoggedInUser                                                                 | i |
| Show own ads        | **Condition to show**<ul><li>Member</li><li>Logged in</li><li>Published at least one ad</li></ul><br>**Expected**<br>All the published ads of a member are shown in the menu 'My Rooms' under the section 'My Advertisements'. <br><br>**Main Actor**<br>LoggedInUser                                                                               | i |

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

| Title                              | Description                      | Status |
|:-----------------------------------|:---------------------------------|:---------|
| Create alerts                       | **Expected**<br>Users can create an alert by setting up the criteria for matching new flats/ads. Whenever a new ad is published matching the search criteria, an alert will be sent to the user. <br><br>**Main Actor**<br>LoggedInUsers                                                   | i |
| Manage active alerts             | **Expected**<br>Below the alerts creation section, active alerts are listed and can be deleted via a button.  <br><br>**Main Actor**<br>LoggedInUsers                                                                 | i |

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


#### 3.1.5 Alerts
Alerts are a kind of automatic search. The following section explains the main use cases of this feature. 

| Title                              | Description                      | Status |
|:-----------------------------------|:---------------------------------|:---------|
| Create alerts                       | **Expected**<br>User can create an alert by setting up the criteria for matching new flats/ads (similar to search criterias). If the criteria are valid (either "Room", "Studio" or both selected and a City selected), a new alert is created and added to the user's "active alerts". Whenever a new ad is published matching the search criteria, an alert will be sent to the user. <br><br>**Main Actor**<br>LoggedInUsers                                                   | i |
| Manage active alerts             | **Expected**<br>Below the alerts creation section, all active alerts are listed. If the "Delete"-Button is pressed, the alert is removed from the "active alerts". <br><br>**Main Actor**<br>LoggedInUsers                                                                 | i |
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