#!/usr/bin/python -tt
# Team HackerRepublic(Hackocracy, HackerEarth).
# Licensed under the Apache License, Version 2.0
# http://www.apache.org/licenses/LICENSE-2.0

# Sarkar Salahkar App
# http://github.com/SarkarSalahkar

"""
    expertSelection.py
    
    This performs the core step in maintaining the quality of the community, 
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
import csv
import json
import firebaseHelper
import random
import userProfileManager

# Firebase project with which this is linked.
firebase_project_name = 'learning-4ae32'

"""
    Triggered when the app requests the server to get the experts so that all the factors mentioned
    above can be taken into account. The approach towards doing this is on an experimental basis, as
    we find better ways to do this, we'll keep updating this.
    The sample input consists of the user id of the user, his avg.level of reputation in tags relevant,
    and a list of tags for which an expert has to be recommended.
"""
def getExperts(writing_user_id, writer_level, tags, targetAuth=None):
    
    # NOTE: remove this comment after we are done.
    """
        Write this function to recommend experts here.
        Assume that userProfileManager can return you any kind of stats about the user
        or maybe the user's details based on his userid.

        Hereafter, score refers to a floating number which represents the power in that field.
        Please specify the range of score also. Like 0.0 <= escalationRate <= 1.0
        So, we'll do simple score calculations to pass as feature to the ML algorithm.
        Keep it simple 

        Presently, we have to utilize machine learning to recommend experts for tags on basis
        of these factors:
        1. Reputation of experts.
        2. Availability.(assume you can get a list of availabilities on prev days from 
           userProfileManager and compute a score from it.)
        3. Escalation rate => get number escalated as of now from userProfileManager and total number
        4. Expert in domains => assign scores (experts are people who have demonstrated record of offline performance). 
        5. Their impact per bad idea=> gets the value as a score that how many ideas have reached their target,
           again a number that can be retrieved by userProfileManager, divided by the ideas that got descalated.

        Approach => maybe clustering (decide yourself)

        This is during the process when new experts are added i.e. users get promoted(all this happens then only I think).
        If that's the case, then move this to the new function promoteUserToLevel(userID, level)

        getting the experts is maybe a simplest procedure of predicting using the model.

    """

    # Returns a list of userIds of the recommended experts. (Don't return more than 8).
    return ['uid1', 'uid2']
    
# Returns the average reputations for the tags.
# output score range: 0 <= rep <= any possible value 
def getExpertsAvgReputationForTags(expert, tags):
    avg_rep = 0
    for tag in tags:
        # Gets reputation for the userID passed for the tag that is given.
        repi = userProfileManager.getReputationForTag(expert, tag)
        avg_rep += repi
    return avg_rep/len(tags)

# Returns a score for availability of the user on platform.
# Range: 0 to 1.0
def getExpertUsersEngagementAvailability(expert):
    # Returns an array of numbers denoting how many hours on each day the user used our app.
    per_day_availabilities = userProfileManager.getAvailabilityForLastMonth(expert)
    avg_avail = 0
    for a in per_day_availabilities:
        avg_avail += a
    return avg_avail/24

# Gets a score based on escalation rate = Solutions escalated/total received.
# provides a measure of activeness. 0 to 1.0
def getEscalationRateForUser(expert):
    solutions_escalated_in_last_month = userProfileManager.getEscalationsInLast30Days(expert)
    solutions_received = userProfileManager.getTotalCountForLastMonth(expert)
    return len(solutions_escalated_in_last_month)/solutions_received

# Returns the impact score, denoting the quality impact made by the expert.
# Range = 0.0 to 1.0
def getImpactScore():
    num_ideas_to_destination = userProfileManager.getNumIdeasReachedDestination(expert)
    total_escalated = userProfileManager.getNumEscalationsForever(expert)
    return num_ideas_to_destination/total_escalated

# Returns a value between 0 to 10.0 denoting the value of the expertness, valuation, 
# knowledge and real life position of the user in the offline world. This is also done by matching with tags.
def getExpertInOfflineWorldScore(expert):
    score = 0
    offline_expert_in = userProfileManager.getOfflineSpecializations(expert)
    for tag in tags:
        if tag in offline_expert_in:
            score += random(0, 1)
            # currently doing randomly because not required as a core for hackathon. 
    return score
    