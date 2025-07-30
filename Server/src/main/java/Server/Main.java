package Server;

import Domain.Validators.AngajatValidator;
import Domain.Validators.BiletValidator;
import Domain.Validators.SpectacolValidator;
import Networking.Server.ObjectConcurrentServer;
import Networking.Server.ProtobuffConcurrentServer;
import Repository.Angajat.AngajatDBHibernateRepo;
import Repository.Angajat.AngajatDBRepo;
import Repository.Bilet.BiletDBRepo;
import Repository.Spectacol.SpectacolDBHibernateRepo;
import Repository.Spectacol.SpectacolDBRepo;
import Service.Angajat.AngajatService;
import Service.Bilet.BiletService;
import Service.Service;
import Service.Spectacol.SpectacolService;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            Properties properties = getProperties();
            AngajatValidator angajatValidator = new AngajatValidator();
            BiletValidator biletValidator = new BiletValidator();
            SpectacolValidator spectacolValidator = new SpectacolValidator();

            AngajatDBRepo angajatDBRepo = new AngajatDBRepo(properties, angajatValidator);
            // AngajatDBHibernateRepo angajatDBRepo = new AngajatDBHibernateRepo();
            SpectacolDBRepo spectacolDBRepo = new SpectacolDBRepo(properties, spectacolValidator);
            // SpectacolDBHibernateRepo spectacolDBRepo = new SpectacolDBHibernateRepo();
            BiletDBRepo biletDBRepo = new BiletDBRepo(properties, spectacolDBRepo, biletValidator);

            AngajatService angajatService = new AngajatService(angajatDBRepo);
            SpectacolService spectacolService = new SpectacolService(spectacolDBRepo);
            BiletService biletService = new BiletService(biletDBRepo);

            Service service = new Service(angajatService, biletService, spectacolService);
            // var server = new ObjectConcurrentServer(15000, new ServiceImplementation(service));
            var server = new ProtobuffConcurrentServer(15000, new ServiceImplementation(service));
            server.start();
        } catch (Exception exception) {
            System.err.println("Database connection failed: " + exception.getMessage());
        }
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("DBCredentials.properties"));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        return properties;
    }
}