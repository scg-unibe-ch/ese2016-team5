## Bug report

### Short description
It is possible to delete a picture if an ad even you click on "Cancel".

### Reproduction
- Go to myRooms
- Click on an ad
- Click on "EditAd"
- Delete a picture
- Click on "Cancel"

### Expected / Observed Behaviour
If you delete a picture when editing an ad you expect that the picture is gone if you click on "Submit". 
But if you click on "Cancel" the picture should still be there. But right now the picture is gone even if 
you click on "Cancel".

### Reason
Picture gets deleted in the database immediately after the click on the "Delete" button and not after 
submitting the change of the ad.
