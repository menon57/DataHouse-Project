# DataHouse Take Home Project

## Handling Input/Output
Running the main function inside of the compatibility class takes input as well as produces output in the JSON format (look at "input.json" and "output.json" for reference). It parses "input.json" calculating the averagevalues of each attribute across all of the team members. Then, for each applicant, a compatibility score is computed based on how close the applicant's attribute values are to the average for the team, where each attribute is further adjusted by a weight which can be set according to preference. Once this score has been computed for each applicant, the results are written to the file "output.json". More details are provided in the comments throughout the compatibility class.

## Assumptions
For the purpose of this assignment, we assume that the value of each attribute can range from 1 - 10, and that each applicant only has the attributes "intelligence", "strength", "endurance", 
and "spicyFoodTolerance".
