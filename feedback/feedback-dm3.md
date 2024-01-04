# Commentaires DM3

Corrigé par An Li

Total: 101% (avec bonus)

## Code source Java du programme [42/45]

- Pas une bonne idée de mettre à jour la base de données uniquement à la sortie
  - Si plusieurs clients se connectent à l'application, les autres clients n'ont pas la dernière version des données
- Modification du profil contre-intuitif
  - Pas besoin de demander à l'utilisateur de passer à travers le formulaire d'inscription au complet
    - Recommandation: Afficher le profil puis demander quel(s) champ(s) changer
- Certains aspects n'ont pas de sens
  - Un revendeur doit pouvoir offrir plusieurs types de produits
  - Un revendeur ne peut pas avoir de suiveurs
  - Utiliser un long ou un string pour l'ISBN, un int est limité à 2147483647, mais un ISBN est normalement 13 chiffres
- Quelques petits détails:
  - Afficher le nom et le prénom de l'acheteur au lieu de son pseudo sur l'étiquette d'expédition
  - Normalement, les points d'achat et d'expérience sont la même banque de points
  
## Tests unitaires en JUnit [20/20]

Bien!

## Configuration Maven [5/5]

Bien!

## Production du JAR [5/5]

Bien!

## Manuel utilisateur (README) [5/5]

Bien!

## JavaDocs générés [5/5]

Bien!

## Cohérence entre les modèles et le code [14/15]

- Toujours la redondance entre Cart et WishList
- Diagramme de classes est un peu difficile à lire

## Bonus: Interface graphique [N/A]

## Bonus: Action GitHub [5/5]

Bien!
