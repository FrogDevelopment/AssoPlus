;This file will be executed next to the application bundle image
;I.e. current directory will contain folder AssoManager-1.0.0-SNAPSHOT with application files
[Setup]
AppId={{fr.frogdevelopment.assoplus}}
AppName=AssoManager-1.0.0-SNAPSHOT
AppVersion=1.0
AppVerName=AssoManager-1.0.0-SNAPSHOT 1.0
AppPublisher=Frog Development
AppComments=AssoManager-1.0.0-SNAPSHOT
AppCopyright=Copyright (C) 2015
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName=c:\AssoManager-1.0.0-SNAPSHOT
DisableStartupPrompt=Yes
DisableDirPage=Yes
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=Frog Development
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=AssoManager-1.0.0-SNAPSHOT-1.0
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=AssoManager-1.0.0-SNAPSHOT.ico
UninstallDisplayIcon={app}\AssoManager-1.0.0-SNAPSHOT.ico
UninstallDisplayName=AssoManager-1.0.0-SNAPSHOT
WizardImageStretch=No
WizardSmallImageFile=AssoManager-1.0.0-SNAPSHOT-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "AssoManager-1.0.0-SNAPSHOT\AssoManager-1.0.0-SNAPSHOT.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "AssoManager-1.0.0-SNAPSHOT\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\AssoManager-1.0.0-SNAPSHOT"; Filename: "{app}\AssoManager-1.0.0-SNAPSHOT.exe"; IconFilename: "{app}\AssoManager-1.0.0-SNAPSHOT.ico"; Check: returnTrue()
Name: "{commondesktop}\AssoManager-1.0.0-SNAPSHOT"; Filename: "{app}\AssoManager-1.0.0-SNAPSHOT.exe";  IconFilename: "{app}\AssoManager-1.0.0-SNAPSHOT.ico"; Check: returnFalse()


[Run]
Filename: "{app}\AssoManager-1.0.0-SNAPSHOT.exe"; Description: "{cm:LaunchProgram,AssoManager-1.0.0-SNAPSHOT}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\AssoManager-1.0.0-SNAPSHOT.exe"; Parameters: "-install -svcName ""AssoManager-1.0.0-SNAPSHOT"" -svcDesc ""AssoManager-1.0.0-SNAPSHOT"" -mainExe ""AssoManager-1.0.0-SNAPSHOT.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\AssoManager-1.0.0-SNAPSHOT.exe "; Parameters: "-uninstall -svcName AssoManager-1.0.0-SNAPSHOT -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
