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
import neuralNetHelper as neuralNets

# Firebase project with which this is linked.
firebase_project_name = 'learning-4ae32'

"""
    Represents experts with following parameters:
    1. user_id
    2. availability score: score assigned on the basis of availability of user.
    3. escalation rate of the expert
    4. score according to reputation of user with the tags specified.
    5. impact score: score on basis of how many ideas have reached their target.
    6. expertise score of user in the offline world.
"""
class Expert(object):
    def __init__(self, user_id, availability_score, escalation_rate, reputation_score, impact_score, expertise_score):
        self.user_id = user_id
        self.availability_score = availability_score
        self.escalation_rate = escalation_rate
        self.reputation_score = reputation_score
        self.impact_score = impact_score
        self.expertise_score = expertise_score

"""
    Triggered when the app requests the server to get the experts so that all the factors mentioned
    above can be taken into account. The approach towards doing this is on an experimental basis, as
    we find better ways to do this, we'll keep updating this.
    The sample input consists of the user id of the user, his avg.level of reputation in tags relevant,
    and a list of tags for which an expert has to be recommended.
"""
def getExperts(writing_user_id, writer_level, tags, targetAuth=None):
    expert_level = writer_level + 1;
    # Provides list of experts at expert_level = writer_level + 1.
    experts_list = firebaseHelper.getExpertsList(expert_level);
    # List of experts contatining Expert class objects. Each object contains user_id, availability score,
    # escalation rate, tags reputation score, impact score and expertise score.
    experts_list_with_parameters = []
    # Provides the list of experts with level = writing user level + 1 with all the parameters like availability
    # score etc. These parameters are then used to assign final scores to expers using neural network.
    for expert in experts_list :
        user_id = expert.user_id
        availability_score = getExpertUsersEngagementAvailability(expert)
        escalation_rate = getEscalationRateForUser(expert)
        reputation_score = getExpertsAvgReputationForTags(expert, tags)
        impact_score = getImpactScore(expert)
        expertise_score = getExpertInOfflineWorldScore(expert)
        expert_param = new Expert(user_id, availability_score, escalation_rate, reputation_score, impact_score, expertise_score)
        experts_list_with_parameters.append(expert_param)
    '''
        NeuralNetHelper class function to assign cummulative final scores to the experts. It uses neural 
        networks on the experts assigning them cummulative scores of the basis of:
        1. availability score of experts
        2. escalation rate of experts
        3. repuatation score with tags
        4. impact score
        5. expertise score
        Values are normalized and then backpropagation is used to train the neural model. It returns the 
        list of experts with their scores and user ids in ascending order of scores.

        Sample input:
        experts_list_with_parameters = [<1, 0.5, 0.6, 7, 0.5, 0.1>, <2, 0.7, 0.4, 8, 0.9, 0.4>]
        It is the list containing two experts with thier user_id, availability_score, escalation_rate,
        reputation, impact_score, expertise_score.

        Sample Output: 
        expert_list_with_sorted_cummulative_scores = [<2, 0.8>, <1, 0.6>]
        It is the list with user_id and cummulative score calculated by neural network in decreasing order of 
        scores.
    '''
    expert_list_with_sorted_cummulative_scores = neuralNets.assign_cummulative_sores(experts_list_with_parameters)
    # Returns best 8 matching experts.
    return expert_list_with_sorted_cummulative_scores[:8]
    
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
def getImpactScore(expert):
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
    