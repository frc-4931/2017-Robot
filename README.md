# 2017-Robot

This repository contains the Java code for our 2017 competition robot.

## Prerequisites

FRC 4931 students should all have their Fedora laptops configured with the development tools, and if so they can skip this section. 

### Build locally

Eclipse should try to automatically compile your code when it starts, and it will automatically compile in the background as you make changes. Eclipse provides tools to quickly run one or more unit tests, and you can even use Eclipse to deploy your code.

However, we encourage you to learn how to compile the code using a terminal:

    $ cd CompetitionRobot
    $ ant compile

You can compile and run all of the tests, too:

    $ ant clean test

and, when ready, deploy the code to the robot (if you're connected to the robot's network):

    $ ant deploy

Regardless of whether you compile and run tests within Eclipse or at a terminal, you are expected to compile and run *all unit tests* before creating a pull-request.

### Get the latest code

As the team committers review and merge pull requests from you and other developers into the team's repository, you'll occassionally want to pull those changes from the team's repository (aka, "upstream") into your own local repository. First, make sure you're on the `master` branch, and then fetch all changes and pull the changes on `master` into your branch:

    $ git checkout master
    $ git pull upstream

Eclipse should automatically detect the changes and recompile, though you may want to run the unit tests to be sure everything works:

    $ ant clean test

Now you can make changes using a topic branch.

### Make your changes

To work with the softwareAs with the other repository, you'll follow the same [development process](https://github.com/frc-4931/2014/wiki/Java-Development-Steps) for each of the issues you'll work on. The basics are:

#### Create an issue

Only make changes that relate to a specific issue, so if one does not exist you need to [create an issue](https://github.com/frc-4931/2017-Robot/issues) that describes your task. 

#### Create a topic branch

A *topic branch* is where you do all your work associated with a specific topic (or issue). You'll have to pick a name that makes sense, but sometimes its easiest to base it on the issue number. For example, if your issue number is 999, then you might use "issue-999" for a topic branch:
```
$ git checkout -b issue-999
```

This command creates the new topic branch with that name, and checks you out onto that branch.

#### Make and test your changes for the issue

Make your changes to the code in Eclipse (which compiles automatically upon save), and optionally compile using the command line:

```
$ ant compile
```

If possible, test your changes on the robot (first make sure you're connected to the robot's network):

```
$ ant deploy run
```

#### Commit your changes

Once you're happy with your changes, commit them to the history on your (local) topic branch:

```
$ git commit -m "Issue 999 - A useful message that summarizes what you did" .
```

and then push your topic branch up to your fork:

```
$ git push origin issue-999
```

#### Create a pull request

In your browser go to https://github.com/frc-4931/2017-Robot and create a pull request for your topic branch. The mentors/reviewers will get a notification, and will review your code. See our [documentation](https://github.com/frc-4931/2014/wiki/Java-Development-Steps#step-8-push-to-github-and-create-a-pull-request) for more detail.

Then switch back to the `master` branch:

    $ git checkout master
    
and continue working on another issue using a separate topic branch.

#### Update your pull request

The reviewers may ask you to make changes to your PR. To do this, change to your topic branch for the issue:

    $ git checkout issue-999

Make the changes, run the unit tests, commit your changes (using a good description message), and finally push your changes:

    $ ant clean test
    $ git commit -m "Issue 999 - Additional changes as requested" .
    $ git push origin issue-999

This will automatically update the PR with your latest changes. Then go to the PR page and add a comment describing your recent changes so that the reviewers know to look again at your work.

#### Rebase your branch

Sometimes other pull requests and changes are merged onto the `master` branch before your pull request, and if similar areas of the code are changed your pull request might need to be _rebased_. This process takes the changes you've made on your branch and bases them on the latest on the `master` branch.

First, make sure you have no uncommitted changes in your workspace and then switch to the `master` branch and get the latest:

    $ git checkout master
    $ git pull upstream master

If this generates any errors, you probably have uncommitted changes on your `master` branch. (This is why we never should develop and create pull requests using your `master` branch.)

If the `git pull` was successful, switch to your topic branch, which in this example is `issue-999`:

    $ git checkout issue-999

and rebase your changes on the latest `master`:

    $ git rebase master

If there are commits on `master` that conflict with changes in your local topic branch, git may have problems automatically resolving them, and will ask you to resolve them manually and then run `git add` for each file and finally run `git rebase --continue`. You may need to do these steps multiple times, depending upon how many commits are on `master` that conflicted with your changes.

Once you've completed the rebase, run the tests just to be sure you've not broken anything:

    $ ant clean test

and if they pass then push your local commits up to your pull request:

    $ git push -f origin issue-999

The reviewers will get a notification and review your changes.

