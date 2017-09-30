#!/usr/bin/python -tt
# Team HackerRepublic(Hackocracy, HackerEarth).
# Licensed under the Apache License, Version 2.0
# http://www.apache.org/licenses/LICENSE-2.0

# Sarkar Salahkar App
# http://github.com/SarkarSalahkar

"""
    server_flask.py
    
    Starts the flask server to listen to incoming API requests on the host.
    The API requests trigger the application of machine learning algorithms
    and the addition of new data points. 
    Most of the updates aren't triggered by the app directly.
    Firebase acts as an intermediate, hence freeing us from database management
    tasks and small backend calculations that could be easily performed using 
    Firebase Cloud Functions.

    The API endpoints are required for the following tasks:
    1. A new task has been added to the firebase realtime db and it contains
       parameters from which the system can train itself.
    2. The app requests the experts from the text entered by the user.

    The second step makes up the core of the app. It is a 2-step process:
    1.  Analyzing the text to gather an understanding of what is being discussed
        based on previous knowledge.
        This step has been delegated to the on device tasks currently and a Java
        based machine learning library is being used for this. However, there is
        a potential to make this better generating large datasets of Indian 
        Authorities, organizations, NGOs etc. Operations like stemming, matching
        of words are performed to gain a list of tags associated with the solution
        being referred.

    2.  This performs the core step in maintaining the quality of the community, 
        matching the suggested solutions with people from within the community, 
        normal users who have been promoted because of their impact, to become 
        subject-level-experts. The solutions are escalated to them on the basis of
        several factors, which are as follows:
        a) Their past performance on the platform
        b) Their reputation
        c) Their escalation history
        d) How good are the ideas that they escalate(i.e. a percentage measure of 
           acceptance by higher authorities)
        e) Previous history with the current user.
        f) Availibility
        g) Their expertise in subject (this also comes from the designation of a person
           eg. Senior officials who have proven record in the offline world of expertise)
        
        The ideas are evaluated by these one-level-above(L+) experts and a decision to 
        escalate or not is made by the expert. The judging by an expert depends on him 
        (don't worry he won't reject good ideas because then he would not gain rep, and 
        he won't escalate bad ideas because then he'll lose rep). So, reputation helps in
        maintaining the quality of our community.
    
    Other tasks include upvote, join in, show support etc.

    Using these methods, Sarkar Salahkar can guarantee a platform that solves one of the 
    biggest problems in our democracy -> Bridging the gap between citizens and government
    in decision making and representation.
    A platform that ensures
        -> Quality of the solutions on platform.
        -> No overwhelming/bombarding requests to authorities
        -> Ensuring best solutions which are really impactful don't get lost amongst billions of others.
        => Simplicity to make it suitable to every Indian citizen.
        -> Spammers unable to clutter the platform and flood irrelevant posts.
"""
from flask import Flask, request, jsonify
import expertSelection
import userProfileManager
import csv
import json

# Initialize the flask app.
app = Flask(__name__)

# Firebase project with which this is linked.
firebase_project_name = 'learning-4ae32'


# Points to the root url, used to test the server. Shows that the
# server is working if the base url is reachable. 
@app.route('/')
def index():
    status = {
        'appName':'SarkarSalahkar',
        'teamName':'HackerRepublic',
        'hackathon':'Hackocracy by HackerEarth',
        'status':'Working correctly'
    }
    return json.dumps(status)


# This is triggered after a new post has been added to the firebase
# realtime database. The addition of a new post will cause the cloud
# function to trigger this url and addition of a data point to the
# previously clustered network.
@app.route("/newPostAdded", methods=['POST'])
def newPostAdded():
    postKey = request.json["postKey"]
    post = request.json["post"]
    # Post key gives us the key as in firebase database while
    # post gives us a python dictionary to refer the post contents.
    tags = request.json["tags"]
    # list of tags identified. Later on, these will also be a backend job.
    originating_level = request.json['origLevel']
    current_level = request.json['currentLevel']
    postObject = {
        'postKey': postKey,
        'post': post,
        'tags':tags,
        'originating_level':originating_level,
        'current_level':current_level
    }
    # No operation to perform right now except saving the post->tag
    # mapping that will be used later on.
    post_tags_map = open('post_tags_map', 'w')
    post_tags_map.write(json.dumps(post_tags_map))
    post_tags_map.close()
    return "Added."

# Called when a post is upvoted by a common user. 
# Note that the user escalating here isn't the expert to whom it
# was escalated to. This user has escalated because the idea has 
# reached his news feed due to support and escalation by experts.
# So, when an idea gains popularity it will be inflated in the quality
# driven community. 
@app.route("/upvote", methods=['POST'])
def postUpvotedByCommonUser():
    postKey = request.json['postKey']
    upvoter = request.json['upvoter']
    # Not implementing this, because not a core functionality.
    return "Noted"

# Called when the post has been escalated, for the user escalating, 
# the preferences need to be updated. 
@app.route("/postEscalated", methods=['POST'])
def postEscalated():
    postKey = request.json["postKey"]
    post = request.json["post"]
    # Post key gives us the key as in firebase database while
    # post gives us a python dictionary to refer the post contents.
    escalating_user = ['escalating_user']
    # The escalating user refers to the expert who escalated.
    tags = request.json["tags"]
    # list of tags identified. Later on, these will also be a backend job.
    originating_level = request.json['origLevel']
    current_level = request.json['currentLevel']
    postObject = {
        'postKey': postKey,
        'post': post,
        'tags':tags,
        'originating_level':originating_level,
        'current_level':current_level,
        'escalating_user':escalating_user,
    }
    # since the user has escalated, we need to keep a track of this
    # and also update the user's preferences. for eg.: say a user escalated
    # the idea of railways, then update his preference towards other railway ideas.
    userProfileManager.updatePreferences(escalating_user, tags)
    return "Noted."

@app.route('/textAnalyze', methods=['POST'])
def textAnalyze():
    title = request.json['title']
    ideaDesc = request.json['description']
    # NOTE: Currently this is being done on device side using Java libraries,
    # We might want to move this to server later, if accuracy seems low.
    return "Not implemented, Perform on device side."

# Performs the core task of getting the list of experts when we are given
# the tags that were referred in post and whose subject experts are required.
# The task of subject expert
@app.route('/getExperts', methods=['POST'])
def getExperts():
    writing_user = request.json['writer']
    writer_level = request.json['writer_level']
    tags = request.json['tags']
    # this request is delegated to the machine learning algorithm to get
    # the experts while keeping in mind the factors mentioned above.
    list_top_few = expertSelection.getExperts(writing_user, writer_level, tags)
    # since this is a list, this needs to be converted to json array.
    return json.dumps(list_top_few)

# When a user's rep points become sufficient to promote him, the firebase cloud functions
# trigger this API endpoint to promote the user to next level.
@app.route('/promote', methods=['POST'])
def promoteUser():
    userID = request.json['userID']
    current_level = request.json['current_level'] 
    expertSelection.promoteUserToLevel(userID, current_level)
    return "promoted"


# Main entry point to start the app.
if __name__ == '__main__':
    app.debug=True
    app.run(host = '0.0.0.0')
    # 0.0.0.0 will start the server on all IPs of the current machine.
    # else in debug mode it only starts on 127.0.0.01
