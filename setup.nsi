;======================================================
; Includes

  !include MUI.nsh
  !include Sections.nsh
  !include target\project.nsh

;======================================================
; Installer Information

  Name "${PROJECT_NAME}"
  OutFile "target\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}-install.exe"
  SetCompressor /SOLID lzma
  XPStyle on
  CRCCheck on
  InstallDir "C:\Program Files\${PROJECT_NAME}\"
  AutoCloseWindow false
  ShowInstDetails show
  Icon "${NSISDIR}\Contrib\Graphics\Icons\orange-install.ico"

;======================================================
; Version Tab information for Setup.exe properties

  VIProductVersion 2008.3.22.0
  VIAddVersionKey ProductName "${PROJECT_NAME}"
  VIAddVersionKey ProductVersion "${PROJECT_VERSION}"
  VIAddVersionKey CompanyName "${PROJECT_ORGANIZATION_NAME}"
  VIAddVersionKey FileVersion "${PROJECT_VERSION}"
  VIAddVersionKey FileDescription ""
  VIAddVersionKey LegalCopyright ""

;======================================================
; Variables


;======================================================
; Modern Interface Configuration

  !define MUI_HEADERIMAGE
  !define MUI_ABORTWARNING
  !define MUI_COMPONENTSPAGE_SMALLDESC
  !define MUI_HEADERIMAGE_BITMAP_NOSTRETCH
  !define MUI_FINISHPAGE
  !define MUI_FINISHPAGE_TEXT "Thank you for installing ${PROJECT_NAME}."
  !define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\orange-install.ico"

;======================================================
; Modern Interface Pages

  !define MUI_DIRECTORYPAGE_VERIFYONLEAVE
  !insertmacro MUI_PAGE_LICENSE .\LICENSE
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

;======================================================
; Languages

  !insertmacro MUI_LANGUAGE "English"

;======================================================
; Installer Sections

Section "DailyWork"
    SectionIn RO
    SetOutPath $INSTDIR
    SetOverwrite on
    File "target\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe"
    writeUninstaller "$INSTDIR\dailywork_uninstall.exe"
SectionEnd

Section "Start menu shortcuts"
    SetShellVarContext all
    CreateDirectory "$SMPROGRAMS\${PROJECT_NAME}"
    CreateShortCut "$SMPROGRAMS\${PROJECT_NAME}\DailyWork.lnk" "$INSTDIR\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe" "" "$INSTDIR\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe" 0
    CreateShortCut "$SMPROGRAMS\${PROJECT_NAME}\Uninstall.lnk" "$INSTDIR\dailywork_uninstall.exe" "" "$INSTDIR\dailywork_uninstall.exe" 0
SectionEnd

Section "Desktop shortcut"
    SetShellVarContext all
    CreateShortCut "$DESKTOP\DailyWork.lnk" "$INSTDIR\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe" "" "$INSTDIR\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe" 0
SectionEnd

Section "Quick launch shortcut"
    SetShellVarContext all
    CreateShortCut "$QUICKLAUNCH\DailyWork.lnk" "$INSTDIR\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe" "" "$INSTDIR\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe" 0
SectionEnd

; Installer functions
Function .onInstSuccess

FunctionEnd

Section "uninstall"
  SetShellVarContext all

  delete "$SMPROGRAMS\${PROJECT_NAME}\DailyWork.lnk"
  delete "$SMPROGRAMS\${PROJECT_NAME}\Uninstall.lnk"
  RMDir "$SMPROGRAMS\${PROJECT_NAME}"
  
  delete "$QUICKLAUNCH\DailyWork.lnk"
  delete "$DESKTOP\DailyWork.lnk"
  
  delete "$INSTDIR\${PROJECT_ARTIFACT_ID}-${PROJECT_VERSION}.exe"
  delete "$INSTDIR\dailywork_uninstall.exe"
  RMDir "$INSTDIR"
SectionEnd

Function .onInit
    InitPluginsDir
FunctionEnd