# Commentaires DM2

Corrigé par An Li

Total: 94%

## Description du système opérationnel [5/5]

Bien!

## 6 diagrammes d'activité UML [22/25]

- Diagramme principal
  - Ne pas montrer toutes les options, juste l'inscription, la connexion et le menu principal par rôle sont suffisants
- Passer une commande
  - Citer dans les hypothèses que l'acheteur a déjà les produits qu'il veut acheter dans son panier
- Offrir un produit à vendre
  - Le revendeur n'a pas à spécifier le nombre de points de base, c'est calculé automatiquement à partir du prix (1 point par $ arrondi à la baisse)

## Diagramme de classes UML [16/20]

- Redondant de mettre les méthodes d'initialisation
- Redondance: Cart = WishList?
- Utiliser les contrôleurs pour mettre les méthodes qui interagissent avec d'autres classes
  - e.g., findProduct(), sellProduct(), getOrderHistory(), etc.
- Indiquer le type des éléments des collections
  - e.g., products: ArrayList<Product>
- Une évaluation n'a qu'un seul auteur

## 5 diagrammes de séquence UML [22/25]

- Vous n'êtes pas obligé de commencer avec la méthode displayMenu()
- Pas une bonne idée d'utiliser des variables tels que 'input0', 'input1', 'input2', etc. Ça devient difficile à suivre.
- Les diagrammes sont un peu chargés, mais on comprend

## Code source Java du programme et fichier JAR [24/25]

Quelques détails à régler:

- L'état d'une commande doit être "En livraison" avant de pouvoir confirmer sa réception
- Il faut avoir acheté le produit avant de pouvoir écrire une évaluation sur celle-ci

## Bonus: Utilisation de GitHub [5/5]

Bien!
