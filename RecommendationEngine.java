import java.io.*;
import java.util.*;

public class RecommendationEngine {

    static Map<String, Set<String>> userPreferences = new HashMap<>();

    public static void main(String[] args) {
        loadDataset("sample_dataset.csv");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to get recommendations: ");
        String user = scanner.nextLine().trim();

        if (!userPreferences.containsKey(user)) {
            System.out.println("❌ User not found in dataset.");
            return;
        }

        Set<String> recommendations = getRecommendations(user);

        System.out.println("\n✅ Recommended products for " + user + ":");
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations found.");
        } else {
            for (String product : recommendations) {
                System.out.println("👉 " + product);
            }
        }
    }

    static void loadDataset(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String user = parts[0].trim();
                String product = parts[1].trim();

                userPreferences.putIfAbsent(user, new HashSet<>());
                userPreferences.get(user).add(product);
            }

        } catch (IOException e) {
            System.out.println("❌ Error reading dataset: " + e.getMessage());
        }
    }

    static Set<String> getRecommendations(String targetUser) {
        Set<String> targetProducts = userPreferences.get(targetUser);
        Set<String> recommended = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : userPreferences.entrySet()) {
            String otherUser = entry.getKey();
            if (otherUser.equals(targetUser)) continue;

            Set<String> otherProducts = entry.getValue();

            // Check for overlap (common products)
            Set<String> common = new HashSet<>(targetProducts);
            common.retainAll(otherProducts);

            if (!common.isEmpty()) {
                // Recommend what the other user liked but target user hasn't
                for (String product : otherProducts) {
                    if (!targetProducts.contains(product)) {
                        recommended.add(product);
                    }
                }
            }
        }
        return recommended;
    }
}
