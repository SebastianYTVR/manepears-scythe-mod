# ManePear's Scythe - Fabric 1.21.1

A Minecraft Fabric mod that adds ManePear's signature Scythe with overpowered enchantments.

## What This Mod Does

- **New item**: Scythe (13 attack damage, -1.5 attack speed)
- **Auto-enchants** when held:
  - Sharpness 10
  - Knockback 1
  - Wind Burst 2
  - Sweeping Edge 3
  - Breach 5
  - Density 5
  - Unbreaking 2147483647 (integer limit)
- **Shield disable** on hit (like axe)
- **Crafting recipe**: 3 netherite ingots + heavy core + wind charge (see `recipe/scythe.json`)

## How to Get the `.jar` File

### Option 1: GitHub Actions (Easiest — No Java Required)

Since this repository doesn't have a `gradlew` wrapper, GitHub Actions will build the mod for you using the installed Gradle action.

**Step 1**: Create the workflow file manually (GitHub API blocks this from being pushed automatically)

1. Go to https://github.com/SebastianYTVR/manepears-scythe-mod
2. Click **"Add file" → "Create new file"**
3. Type the filename: `.github/workflows/build.yml`
4. Paste this exact content:

```yaml
name: Build Fabric Mod

on:
  push:
    branches: [main]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3
      - name: Build with Gradle
        run: gradle build
      - name: Upload artifact
        uses: actions/upload-artifact@v4
        with:
          name: scythe-mod
          path: build/libs/*.jar
```

5. Click **"Commit changes..."**
6. The build will start automatically!

**Step 2**: Download the built `.jar`

1. Go to **Actions** tab (next to Pull Requests)
2. Click the workflow run that just started
3. Wait a few minutes for it to complete
4. Scroll to the bottom and find the **"Artifacts"** section
5. Download `scythe-mod`
6. The `.jar` file inside is your mod — drop it in your `mods` folder

### Option 2: Build Locally

**Requirements**: Java 21 installed

```bash
# Clone the repo
git clone https://github.com/SebastianYTVR/manepears-scythe-mod.git
cd manepears-scythe-mod

# Generate Gradle wrapper (optional but recommended)
gradle wrapper

# Or if you have Gradle installed:
gradle build

# Find the built jar:
ls build/libs/*.jar
```

The output will be `build/libs/manepears-scythe-mod-1.0.0.jar` (or similar).

## Installing the Mod

1. Install **Fabric Loader** for Minecraft 1.21.1 from https://fabricmc.net/
2. Place the downloaded `.jar` in your `.minecraft/mods/` folder
3. Launch Minecraft with the Fabric profile

## Adding a Texture

The mod expects a texture at:
```
src/main/resources/assets/manepearsscythe/textures/item/scythe.png
```

You can add your own 16x16 (or higher) PNG texture and rebuild, or it will show the purple-black missing texture in-game until you add one.

## Known Build Notes

- The enchantments are applied **at runtime** when you hold the item, not at crafting time
- Shield disable works on both main hand and off hand shields
- This mod targets Fabric Loader 0.16.9+ for Minecraft 1.21.1

## License

MIT
