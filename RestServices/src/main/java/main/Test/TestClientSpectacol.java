package main.Test;

import Domain.Spectacol;
import main.Client.SpectacolClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TestClientSpectacol {
    private static final SpectacolClient client = new SpectacolClient();
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {
            testGetById();
            testCreateAndDelete();
            testGetAll();
            testUpdate();
        } catch (Exception e) {
            System.err.println("\n Test failed: " + e.getMessage());
        }
    }

    private static void testGetById() {
        System.out.println("\n=== TESTING GET BY ID ===");
        int testId = 10;
        Spectacol spectacol = client.getById(testId);
        printSpectacol("Spectacol with ID " + testId, spectacol);
    }

    private static void testCreateAndDelete() {
        System.out.println("\n=== TESTING CREATE AND DELETE ===");
        Spectacol newSpectacol = new Spectacol(
                "Magician Ultimate",
                LocalDateTime.now(),
                "Cluj Arena",
                50,
                10
        );

        System.out.println("Creating new spectacol...");
        Spectacol created = client.create(newSpectacol);
        printSpectacol("Created spectacol", created);

        System.out.println("\nDeleting spectacol with ID: " + created.getId());
        client.delete(created.getId());
        System.out.println("Delete successful");
    }

    private static void testGetAll() {
        System.out.println("\n=== TESTING GET ALL ===");
        Spectacol[] allSpectacole = client.getAll();
        List<Spectacol> spectacoleList = Arrays.asList(allSpectacole);

        System.out.println("Found " + spectacoleList.size() + " spectacole:");
        spectacoleList.forEach(TestClientSpectacol::printSpectacolCompact);
    }

    private static void testUpdate() {
        System.out.println("\n=== TESTING UPDATE ===");
        int testId = 10;
        Spectacol spectacol = client.getById(testId);

        System.out.println("Original values:");
        printSpectacolCompact(spectacol);

        System.out.println("\nUpdating available seats to 15...");
        spectacol.setNrLocuriDisponibile(15);
        client.update(spectacol);
        printSpectacolCompact(client.getById(testId));

        System.out.println("\nUpdating available seats to 17...");
        spectacol.setNrLocuriDisponibile(17);
        client.update(spectacol);
        printSpectacolCompact(client.getById(testId));
    }

    private static void printSpectacol(String header, Spectacol spectacol) {
        System.out.println("\n" + header + ":");
        System.out.println("----------------------------------------");
        System.out.println("ID: " + spectacol.getId());
        System.out.println("Artist: " + spectacol.getNumeArtist());
        System.out.println("Date: " + spectacol.getDataSpectacol().format(formatter));
        System.out.println("Location: " + spectacol.getLocSpectacol());
        System.out.println("Available seats: " + spectacol.getNrLocuriDisponibile());
        System.out.println("Occupied seats: " + spectacol.getNrLocuriOcupate());
        System.out.println("----------------------------------------");
    }

    private static void printSpectacolCompact(Spectacol spectacol) {
        System.out.printf(
                "| %3d | %-20s | %-16s | %-10s | %3d/%3d |%n",
                spectacol.getId(),
                spectacol.getNumeArtist(),
                spectacol.getDataSpectacol().format(formatter),
                spectacol.getLocSpectacol(),
                spectacol.getNrLocuriOcupate(),
                spectacol.getNrLocuriDisponibile()
        );
    }
}