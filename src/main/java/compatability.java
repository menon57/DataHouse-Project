//Author: Aditya Menon
//It is not ok to share my code anonymously

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class compatibility {

    //HashMap to track weights of each attribute
    static HashMap<String, Double> weights = new HashMap<>();

    public static void main(String[] args) throws IOException, ParseException {
        try {
            //Read in JSON Data
            JSONParser parser = new JSONParser();
            FileReader fr = new FileReader(".\\data\\input.json");
            JSONObject obj = (JSONObject)parser.parse(fr);
            //Team information
            JSONArray team = (JSONArray) obj.get("team");
            //Applicant information
            JSONArray applicants = (JSONArray) obj.get("applicants");
            //Number of team members
            int n = team.size();
            //Number of applicants
            int m = applicants.size();

            //Weights for each attribute, change as necessary
            weights.put("endurance", 0.4);
            weights.put("intelligence", 0.3);
            weights.put("strength", 0.2);
            weights.put("spicyFoodTolerance", 0.1);

            //Find average attribute values across team
            JSONObject teamAvg = new JSONObject();
            double avgIntelligence = 0.0;
            double avgStrength = 0.0;
            double avgEndurance = 0.0;
            double avgSpicyFoodTolerance = 0.0;

            //To be written to output.json
            JSONObject ret = new JSONObject();
            JSONArray scores = new JSONArray();

            //Read in and sum each attribute for each team member
            for (int i = 0; i < n; i++) {
                JSONObject member = (JSONObject)team.get(i);
                JSONObject attributes = (JSONObject)member.get("attributes");
                avgIntelligence += Double.valueOf(String.valueOf(attributes.get("intelligence")));
                avgStrength += Double.valueOf(String.valueOf(attributes.get("strength")));
                avgEndurance += Double.valueOf(String.valueOf(attributes.get("endurance")));
                avgSpicyFoodTolerance += Double.valueOf(String.valueOf(attributes.get("spicyFoodTolerance")));
            }

            //Compute the average and represent it on a scale between 0 and 1
            avgIntelligence = (avgIntelligence / n) / 10.0;
            avgStrength = (avgStrength / n) / 10.0;
            avgEndurance = (avgEndurance / n) / 10.0;
            avgSpicyFoodTolerance = (avgSpicyFoodTolerance / n) / 10.0;

            teamAvg.put("intelligence", avgIntelligence);
            teamAvg.put("strength", avgStrength);
            teamAvg.put("endurance", avgEndurance);
            teamAvg.put("spicyFoodTolerance", avgSpicyFoodTolerance);


            //Iterate through each team member and compute compatibility
            for (int i = 0; i < m; i++) {
                JSONObject applicant = (JSONObject)applicants.get(i);
                JSONObject toAdd = new JSONObject();
                toAdd.put("name", applicant.get("name"));
                toAdd.put("compatibility", calculateCompatibility(teamAvg, (JSONObject) applicant.get("attributes")));
                scores.add(toAdd);
            }

            ret.put("scoredApplicants", scores);

            //Write results to output.json
            try (FileWriter fw = new FileWriter(".\\data\\output.json")) {
                fw.write(ret.toJSONString());
                fw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Helper function to compute compatibility. Compatibility is determined based on the distance of an applicant's
    / attribute values from the team average (closer indicating greater compatibility and vice versa) as well as the
    / weights of each attribute according to user specification
     */
    public static double calculateCompatibility (JSONObject avgAttributes, JSONObject applicantAttributes) {
        //Total compatibility score
        double compatibility = 0.0;
        //Score for each respective attribute
        double attributeScore;
        //Value for each applicant's respective attributes
        double applicant_val;
        //Team average for each respective attribute
        double team_val;

        //Iterate through each attribute, calculate its score and adjust it based on determined weight.
        //Add to total compatibility
        for (Object k : avgAttributes.keySet()) {
            applicant_val = Double.valueOf(String.valueOf(applicantAttributes.get(k))) / 10.0;
            team_val = (Double)avgAttributes.get(k);
            attributeScore = 1 - Math.abs(applicant_val - team_val);
            compatibility += weights.get(k) * attributeScore;
        }

        //Return final compatibility score
        return Math.floor(Math.min(Math.max(compatibility, 0), 1) * 10.0) / 10.0;
    }
}
