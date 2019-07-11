# ResponseServer
This contains ways to communicate with MySQL DB via Java

## Pre-requisites
1. Install MySQL on your machine
2. Download MySQL connector JAR

## Steps
Starting the main thread will launch an UI which will allow you to add/delete/view/refresh actions. The initial screen will show all the entries from the DB.
### Add
This will launch another UI which accepts two strings to create an entry in the DB
### Delete
You need to select one entry from the DB to remove it (one at a time)
### View
Check the content of the selected entry (one at a time)
### Refresh
This will refresh the content of the list from the DB
