## Demonstration of responsiveness testing in-sprint

There is a lot of discussions about how to shift left performance testing into the sprint. What can developers test per commit or smoke test to
validate performance degredation and fix it? \
single user performance testing offers an approach to test responsiveness of the client side on top of any functional test.\

Basically one can add:\
- OCR and time analysis for screen elements rendering (in this example we're measuring app launch, login and logout times)\
- For web (desktop or mobile) one can report the page and object timing (based on W3C standards)\
- Offer logs for network interaction (HAR, PCAP), native app crash logs, device/app vitals\
- expand the test scope to include real user conditions, such as sub-optimal network conditions, 'heavy' apps running in the background etc.\

# Run this example
- In this example we're launchign an iPhone-X in Perfecto lab => Ensure to change device ID in src\main\resources\config\testng.xml\
- Enter your Netflix credentials as environment variables: Netflix_Username, Netflix_Password\
- Run ;)
