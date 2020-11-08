
<div style="text-align: center;" align='center'>
    <a href=[plugin-homepage]>
        <img src="./src/main/resources/META-INF/pluginIcon.svg" width="320" alt="logo"/>
    </a>
</div>

<!--
<div style="text-align: center; width: auto;">

[![Plugin Icon][file:plugin-icon.svg]][plugin-homepage]

</div>
-->

<!--
[![Build](https://github.com/JetBrains/intellij-platform-plugin-template/workflows/Build/badge.svg)][gh:build]
-->
[![GitHub](https://img.shields.io/github/license/sleepingraven/idea-setting-explorer)][gh:license]

  - [Introduction](#introduction)
  - [Overview](#overview)
  - [Compatibility](#compatibility)
  - [Installation](#installation)
  - [Usage](#usage)
    - [Shortcuts](#shortcuts)
    - [Tags](#tags)
    - [Search](#search)
  - [Change Log](#change-log)
  - [Support](#support)
  - [Useful links](#useful-links)

## Introduction

<!-- Plugin description -->
With **Idea Setting Explorer** you can search the configurations within your **IntelliJ IDEA** conveniently and view documentations about them.

The main goal of this plugin is to provide guidance of configurations for developers and facilitate the search of them by listing operation steps, describing with pictures, linking to the proper documentation pages, and marking them with colored tags, witch displays in lightweight, concise, fast and immersive popups.
<!-- Plugin description end -->

## Overview

There are two expression ways of the UI on this plugin:

- Catalog Mode - Is used for previewing and searching the configurable.

  ![Catalog mode preview][file:app-preview.png]

- View Mode - Is used for viewing documentations of the configurable.

  ![View mode preview][file:view-mode-preview.png]

## Compatibility

IntelliJ IDEA for versions after 2020.2

## Installation

- Using IDE built-in plugin system:

  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for `Idea Setting Explorer`</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/%REPOSITORY%/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage

The popups active by clicking this action's icon button on **Toolbar**, or pressing <kbd>Ctrl+Alt+Shift+E</kbd> bound to the action, or typing action name `View Settings` on Windows / `View Preferences` on Mac in **Search Everywhere**.

![Active Popup][file:active-popup.png]

### Shortcuts

| Key | Description |
| ---- | ---- |
| <kbd>Tab</kbd> | Switch between *Search Text Field* and *Catalog Tree*. |
| <kbd>↑</kbd>/<kbd>↓</kbd> | Move tree selection up/down. |
| <kbd>←</kbd>/<kbd>→</kbd> | Collapse/Expand the tree nodes selected.<br />If the current focused component is *Search Text Field*, press <kbd>Alt</kbd> + <kbd>←</kbd>/<kbd>→</kbd> instead. |
| <kbd>Page Up</kbd>/<kbd>Page Down</kbd> | Move the catalog page up/down.<br />If the current focused component is *Search Text Field*, press <kbd>Alt</kbd> + <kbd>Page Up</kbd>/<kbd>Page Down</kbd> instead. |
| <kbd>Enter</kbd> | Show the configuration page in **Settings/Preferences** dialog. |
| <kbd>Esc</kbd> | Close the popups viewing. |

In addition, you can also give expansion by clicking mouse <kbd>Main Button</kbd> and jump to **Settings/Preferences** dialog by double clicking.

### Tags

There are several colored tags of configurations, marking them and providing additional information, witch can be searched in *Search Text Field*.

![Tags][file:tags.png]

New tags will be supplemented and improved in the future.

### Search

Once a character typed or pasted in *Search Text Field*, the tree filters configurations, expanding all nodes that matched.

The matching between a pattern and configurations follows the following rules:

1. If exists a **subsequence** of the configuration's name matches the pattern, then matched. Otherwise, go to step 2.
2. If exists a **substring** of one of the configuration's tag matches the pattern, then matched.
3. Once the last character of the pattern matched, it will return and check next configuration, without matching characters left.

Matched characters of configurations will have a highlighted appearance.

![Search][file:search.png]

## Change Log

[0.0.1] - 2020-11-7
- Added
  - Initial project scaffold
  - UI built
  - Configurations added
  - Documentations added
  - README written
  - CHANGELOG written

Refer to [Change Log][gh:change-log] page for complete update log.

## Support

You can support this project through any of the following points:

- Star this project
- Share this plugin with your friends
- Review this plugin on [Reviews][plugin-reviews] page
- Make pull requests
- Report bugs
- Tell us your ideas and suggestions

## Useful links

- [IntelliJ IDEA Documentation][docs:idea]
- [IntelliJ IDEA Documentation of Settings/Preferences][docs:idea-settings-preferences]
- [IntelliJ IDEA Users Forum][jb:users-community]

[file:plugin-icon.svg]: .github/readme/pluginIcon.svg
[file:app-preview.png]: .github/readme/app-preview.png
[file:view-mode-preview.png]: .github/readme/view-mode-preview.png
[file:active-popup.png]: .github/readme/active-popup.png
[file:tags.png]: .github/readme/tags.png
[file:search.png]: .github/readme/search.png

[gh:change-log]: https://github.com/sleepingraven/idea-setting-explorer/blob/master/CHANGELOG.md
[gh:license]: https://github.com/sleepingraven/idea-setting-explorer/blob/master/LICENSE

[plugin-homepage]: https://www.jetbrains.com/help/idea/discover-intellij-idea.html
[plugin-reviews]: https://www.jetbrains.com/help/idea/discover-intellij-idea.html

[docs:idea]: https://www.jetbrains.com/help/idea/discover-intellij-idea.html
[docs:idea-settings-preferences]: https://www.jetbrains.com/help/idea/settings-preferences-dialog.html
[jb:users-community]: https://intellij-support.jetbrains.com/hc/en-us/community/topics/200382555-IntelliJ-IDEA-Users
