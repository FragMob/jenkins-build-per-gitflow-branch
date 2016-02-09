package com.neoteric.jenkins

import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;

@ToString
@EqualsAndHashCode
class TemplateJob {
    String jobName
    String baseJobName
    String templateBranchName

    static String cleanBranchName(String branchName)
    {
        //grabs index of slash that follows feature/, hotfix/, release/
        int x = branchName.indexOf('/')

        //replaces all non-alphanumeric characters to a "dash" source: http://stackoverflow.com/questions/6053541/regex-every-non-alphanumeric-character-except-white-space-or-colon
        branchName = branchName.replaceAll("[^a-zA-Z\\d\\s:]","-").replaceAll(' ','-')

        //replaces consecutive dashes with just one dash
        branchName = branchName.replaceAll("[\\s-]+","-")

        //removes any trailing dash
        if((branchName.lastIndexOf('-')+1) == branchName.length())
        {
            branchName = branchName.substring(0,branchName.length()-1)
        }

        //Adds the "/" back into the branch prefix so that its feature/, hotifx/, release/ instead of feature-, hotfix-, release-
        char[] branchNameChars = branchName.toCharArray();
        branchNameChars[x] = '/';
        branchName = String.valueOf(branchNameChars);

        return branchName
    }


    String jobNameForBranch(String branchName) {
        // git branches often have a forward slash in them, but they make jenkins cranky, turn it into an underscore
        String safeBranchName = branchName.replaceAll('/', '_')
        return "$baseJobName-$safeBranchName"
    }
    
    ConcreteJob concreteJobForBranch(String jobPrefix, String branchName) {
        ConcreteJob concreteJob = new ConcreteJob(templateJob: this, branchName: branchName, jobName: jobPrefix + '-' + jobNameForBranch(cleanBranchName(branchName)) )
    }
}
