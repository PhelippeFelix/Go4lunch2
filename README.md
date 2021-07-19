# Go4lunch2
L'application Go4Lunch
Go4Lunch est une application collaborative utilisée par tous les employés.  
Elle permet de rechercher un restaurant dans les environs, puis de sélectionner celui de son choix en en faisant part à ses collègues.   
De la même manière, il est possible de consulter les restaurants sélectionnés par les collègues afin de se joindre à eux.   
Un peu avant l’heure du déjeuner, l’application notifie les différents employés pour les inviter à rejoindre leurs collègues.  

Go4Lunch se repose sur le back-end Firebase proposé par Google.  

les fonctionnalités de l'applications sont :  

La création de comptes utilisateur.  
L’authentification des utilisateurs via des services tiers (Facebook, Twitter et Google).  
La sauvegarde des données sur Firebase.  
Vue des restaurants sous forme de liste, cette vue permet d’afficher le détail des restaurants qui se situent sur la carte.  
Vue des restaurants sous forme de carte(L’utilisateur est automatiquement géo-localisé par l’application, afin d’afficher le quartier dans lequel il se trouve).  
Fiche détaillée d'un restaurant.  
Liste des collègues (comptes utilisateurs).  
Fonctionnalité de recherche textuelle.  
L’envoi de messages Push.  

Stack technique :  
  
Développement Android Natif Java  
Architectures : MVC  
Qualité : Clean Code, Tests unitaires, Tests instrumentalisés  
Outils : GIT, Gradle, Firebase, GoogleAPIs, Espresso  
