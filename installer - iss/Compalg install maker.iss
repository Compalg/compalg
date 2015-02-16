; -- Example3.iss --
; Same as Example1.iss, but creates some registry entries too.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=CompAlg 2.0
AppVersion=2.0
DefaultDirName={pf}\CompAlg 2.0
DefaultGroupName=CompAlg 2.0
UninstallDisplayIcon={app}\logoCompAlg.ico
OutputDir=userdocs:

[Files]
Source: "CompAlg_2.0.jar"; DestDir: "{app}"
Source: "CompAlg_2.0.lnk"; DestDir: "{app}"
Source: "logoCompAlg.ico"; DestDir: "{app}"; 

[Icons]
Name: "{group}\CompAlg 2.0"; Filename: "{app}\CompAlg_2.0.lnk"

; NOTE: Most apps do not need registry entries to be pre-created. If you
; don't know what the registry is or if you need to use it, then chances are
; you don't need a [Registry] section.

; by David Silva Barrera (dsbarrera@gmail.com)
; copy the Jar file here. Take care of link icon.