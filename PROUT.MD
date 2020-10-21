- Frame (Main Window):
--  [Seems to work] Defaults to the user's appdata (by using the system property APPDATA). Can be use as a portable MC installation
--  Multi-Accounts Support (Needs to support atleast 250+ accs, don't quote me on this)
--  Remember Me: By default only the email will be remembered, uuid, clientToken and accessToken in a file. Maybe better per-files in .minecraft/miniLauncher/accounts/YourAccountName.json
--    A CheckBox will be added to remember your (WARNING I'm aware this could be a security risk, thats why I want to see it encrypted/decrypted) password
----      With this enabled, the launcher checks if the token is still valid. If invalid, non-existent or expired, the launcher will re-auth itself and renews the auth-token if the save-password (The Remember me) is checked (or better moving it to seperate checkbox in settings or hide it into a setting which the user will add him/herself) else the launcher will ask the password again to re-auth.
------ If not checked or not in the config, the launcher will ask the password if the token is invalidated (like most launchers actually)
--  Player head: If using a premium account, the variable to get the username should be a UUID instead. 
--  Non-Vanilla versions such as Forge, LiteLoader, Fabric, Optifine and so on are ignored, it should be visible in the version selector!
--  Console option should be offered when using the selected MC version (Would need to create a consoleController for that)


Thats all I guess.
