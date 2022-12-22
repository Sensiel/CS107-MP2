# Description du jeu ICRogue

ICRogue est un jeu visant principalement à récupérer la clé permettant de déverrouiller la porte verrouillé du niveau pour ensuite tuer les ennemis s’y trouvant. À cet égard, afin de parcourir les différentes aires du niveau et trouver la clé, il faudra relever les défis de chaque salle pour pouvoir débloquer ses portes et accéder ainsi à la salle suivante et ainsi de suite. Plus précisément, il faudra tuer des ennemis et collecter des objets, tout en faisant attention au niveau de vie du joueur, puisque le jeu s’arrête à la mort de ce dernier.


# Comment lancer le jeu

Afin de lancer le jeu, vous devez executer la méthode Main de Play.java dans le dossier ICRogue.
3 méthodes de générations de carte sont mise à votre disposition : 
Level0() : crée un map aléatoire
Level0(false) : crée une map selon un schéma spécifique
Level0(new int[]{4,3,2,1,2,3}) : crée une map selon des caractéristiques INEDITS ainsi qu'une salle EXCLUSIVE et TREPIDANTE

# Solution ICRogue

ICRoguePlayer doit à présent parcourir les salles de son niveau pour trouver la salle du boss et la défaire.


*Controles*
Flèches directionnelles: Deplacement du joueur
W: Récupération du baton magique / Ouverture d’une porte verrouillé
X: Tir de boule de feu
R: Reset le jeu ( le niveau change si il est aléatoire)


## Comment affronter le Turret ?


Le turret est un ennemi de grande taille que l’on ne peut tuer que de 2 manières :


1- En exploitant sa sensibilité au feu : En effet, le Turret ne supporte pas le feu et peut même mourir à son contact. Par conséquent, ICRoguePlayer va utiliser cette information à son avantage, en lui tirant de nombreuses boules de feu dessus afin de le détruire.


2- En lui marchant dessus : En effet, écraser le Turret est une bonne manière de tuer ce dernier, cependant il faudra faire attention à ne pas se faire tuer par ses flèches. 

## Comment savoir si le joueur va mourir ?

Le joueur possède un indicateur de sa vie actuelle, représenté par une barre au dessus de lui. Lorsqu'il se prend des dégats, cette dernière se vide et passe peu à peu du vert clair au rouge.

## Comment produire des boules de feu ?

Pour produire des boules de feu, l’ICRoguePlayer doit trouver la salle contenant le baton magique et récupérer ce dernier grâce à la touche W. Ensuite, il suffira de cliquer sur la touche X pour tirer la boule de feu et ainsi attaquer ses ennemis.


## Faire augmenter son niveau de vie

Une fois que l’ICRoguePlayer est épuisé au point d’avoir un niveau de vie très bas, il faudra penser à chercher des coeurs dans les salles et à les collecter. En effet, le coeur est un bon moyen pour le personnage de se ressourcer et faire augmenter son niveau de vie, prolongeant ainsi son temps de jeu.

##Quel effet ont les plaques de glace ?

Lorsque le joueur arrive sur une plaque de glace, il glissera vers la case devant lui et ce tant qu'il n'arrive pas sur une case normale ou qu'il a un mur devant lui.

## Fin du jeu


Le jeu peut se finir de deux manières : 


1- L’ICRoguePlayer a gagné et a pu relever les défis de toutes les salles du niveau. —> WIN
2- L’ICRoguePlayer a atteint un niveau de vie nulle et est mort vaincu. —> GAME OVER