
<div style='text-align: center;' align='center'>
<!--
<a href='https://plugins.jetbrains.com/plugin/15345-idea-setting-explorer'>
    <img src="./src/main/resources/META-INF/pluginIcon.svg" width="320" alt="Logo"/>
</a>
-->

[![Logo][file:logo.svg]][plugin-homepage]

<h1>Idea Setting Explorer</h1>
<p>Setting Navigation for IntelliJ IDEA.</p>

[![Version](https://img.shields.io/jetbrains/plugin/v/15345-idea-setting-explorer.svg)](https://plugins.jetbrains.com/plugin/15345-idea-setting-explorer)
[![Build](https://github.com/sleepingraven/idea-setting-explorer/workflows/Build/badge.svg)][gh:build]
[![Release](https://img.shields.io/github/v/release/sleepingraven/idea-setting-explorer)][gh:latest-release]
[![License](https://img.shields.io/github/license/sleepingraven/idea-setting-explorer)][gh:license]

</div>

<!--
[![Downloads](https://img.shields.io/jetbrains/plugin/d/15345-idea-setting-explorer.svg)](https://plugins.jetbrains.com/plugin/15345-idea-setting-explorer)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/15345-idea-setting-explorer.svg)](https://plugins.jetbrains.com/plugin/15345-idea-setting-explorer)
[![Rating](https://img.shields.io/jetbrains/plugin/r/stars/15345-idea-setting-explorer.svg)](https://plugins.jetbrains.com/plugin/15345-idea-setting-explorer)
[![Stars](https://badgen.net/github/stars/sleepingraven/idea-setting-explorer/)]()
[![Top Languages](https://img.shields.io/github/languages/top/sleepingraven/idea-setting-explorer)]()
[![Languages](https://img.shields.io/github/languages/count/sleepingraven/idea-setting-explorer)]()
[![Search Counter](https://img.shields.io/github/search/sleepingraven/idea-setting-explorer/hh)]()
[![Code Size](https://img.shields.io/github/languages/code-size/sleepingraven/idea-setting-explorer)]()
[![GitHub Repo Size](https://img.shields.io/github/repo-size/sleepingraven/idea-setting-explorer)][gh:latest-release]
[![Release](https://img.shields.io/github/v/release/sleepingraven/idea-setting-explorer?include_prereleases)]()
-->

<br />

  - [Introduction](#introduction)
  - [Overview](#overview)
  - [Compatibility](#compatibility)
  - [Installation](#installation)
  - [Usage](#usage)
    - [Shortcuts](#shortcuts)
    - [Tags](#tags)
    - [Search](#search)
  - [Change Note](#change-note)
  - [Support](#support)
  - [Useful links](#useful-links)

## Introduction

<!-- Plugin description -->
With **Idea Setting Explorer** you can search the configurations within your **IntelliJ IDEA** conveniently and view documentations about them.

The main goal of this plugin is to provide guidance of configurations for developers and facilitate the search of them by:

- listing operation steps,
- describing with pictures,
- linking to the proper documentation pages,
- marking them with colored tags,

witch displays in lightweight, concise, fast and immersive popups.
<!-- Plugin description end -->

## Overview

There are two expression ways of the UI on this plugin:

- Catalog Mode - Is used for previewing and searching the configurable.

  ![Catalog mode preview][file:app-preview.png]

- View Mode - Is used for viewing documentations of the configurable.

  ![View mode preview][file:view-mode-preview.png]

## Compatibility

IntelliJ IDEA for version 2020.2 or higher.

## Installation

- Using IDE built-in plugin system:

  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Idea Setting Explorer"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release][gh:latest-release] and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage

The popups are activated by one of following performances:

 - Click this action's icon button ![Action Button][file:action-button.png] on **Toolbar**.
 - Press <kbd>Ctrl+Alt+Shift+E</kbd> bound to the action.
 - Type action name `View Settings` on Windows / `View Preferences` on Mac in **Search Everywhere**, enter this action.

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

Additionally, matches are not case-sensitive.

## Change Note

> ### [v0.0.3](https://github.com/sleepingraven/idea-setting-explorer/tree/v0.0.3) (2020/11/12)
> - Fixed
>   - Plugin Description fixed

Refer to [Changelog][gh:change-log] page for the complete update log.

## Support

You can support this project through any of the following points:

- Star this project
- Share this plugin with your friends
- Rating this plugin on [Reviews][plugin-reviews] page
- Make pull requests
- Report bugs
- Tell us your ideas and suggestions

## Useful links

- [IntelliJ IDEA Documentation][docs:idea]
- [IntelliJ IDEA Documentation of Settings/Preferences][docs:idea-settings-preferences]
- [IntelliJ IDEA Users Forum][jb:users-community]

[file:logo.svg]: .github/readme/Logo.svg
[file:plugin-icon.svg]: .github/readme/pluginIcon.svg
[file:app-preview.png]: .github/readme/app-preview.png
[file:view-mode-preview.png]: .github/readme/view-mode-preview.png
[file:action-button.png]: ./src/main/resources/META-INF/miniLogo.svg
[file:active-popup.png]: .github/readme/active-popup.png
[file:tags.png]: .github/readme/tags.png
[file:search.png]: .github/readme/search.png

[gh:latest-release]: https://github.com/sleepingraven/idea-setting-explorer/releases/latest
[gh:change-log]: https://github.com/sleepingraven/idea-setting-explorer/blob/main/CHANGELOG.md
[gh:build]: https://github.com/sleepingraven/idea-setting-explorer/actions?query=workflow%3ABuild
[gh:license]: https://github.com/sleepingraven/idea-setting-explorer/blob/main/LICENSE

[plugin-homepage]: https://plugins.jetbrains.com/plugin/15345-idea-setting-explorer/
[plugin-reviews]: https://plugins.jetbrains.com/plugin/15345-idea-setting-explorer/reviews

[docs:idea]: https://www.jetbrains.com/help/idea/discover-intellij-idea.html
[docs:idea-settings-preferences]: https://www.jetbrains.com/help/idea/settings-preferences-dialog.html
[jb:users-community]: https://intellij-support.jetbrains.com/hc/en-us/community/topics/200382555-IntelliJ-IDEA-Users
