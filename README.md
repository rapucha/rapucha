# rapucha
Small statistics service to monitor available bikes in St-Petersburg municipal bicycle sharing system.

Curren version:
Collects data each 15 minutes and stores them on local fs.

Major Todo:
store data in the DB
implement web view for collected stats.
split the service to make crawler a separate process running on another machine (with county local 3G operator IP address)
