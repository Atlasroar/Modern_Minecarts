# Modern Minecarts Test Mod

This standalone Fabric mod runs in-game tests against the Modern Minecarts mod.
It checks normal-rail speed, the ascending speed cap, powered-rail speed,
copper oxidation speed, and the existing crossing-rail speed.

From the repository root, build the test mod with:

```powershell
.\gradlew.bat :testmod:build
```

Run the GameTests with:

```powershell
.\gradlew.bat :testmod:runGametest
```

The XML test report is written to
`testmod/build/gametest-report.xml`.
