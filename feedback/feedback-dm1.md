# Commentaires DM1

Corrigé par An Li

Total: 92%

## Glossaire [9/10]

- Termes manquants:
  - UniShop

## Diagramme de cas utilisation [20/25]

- Manque le fichier .vpp et le lien vers ce fichier dans le rapport
- Le diagramme est à la mauvaise place dans le rapport
  - Il doit apparaître avant les descriptions des cas d'utilisation
- Trop de croisements entre les lignes, difficile à lire...
- Pour alléger le diagramme, vous pouvez assumer que l'utilisateur est déjà connecté à la plateforme pour diminuer le nombre de relations «include»
- Manque acteurs externes:
  - Fournisseur de paiement
  - Service postal/livreur

## Description des cas d'utilisation [50/50]

- S’inscrire à la plateforme en tant qu’acheteur
  - Il n'y a pas d'acteur secondaire
- Modifier son profil
  - Postcondition devrait être que les modifications au profil soient enregistrés avec succès
- Rechercher un/des produit(s) ou un/des revendeurs
  - Postcondition devrait être que l'acheteur obtient la liste des produit(s)/revendeur(s) correspondant à ses filtres de recherche
- Rembourser un/des produit(s)
  - Devrait être automatique dès la réception du colis retourné. Il a seulement besoin de confirmer la livraison du retour à l'entrepôt
- Passer une commande
  - Le revendeur doit transmettre le colis au service postal avant de fournir les informations de suivi

## Risques [4/5]

La panne de la plateforme cause directement la non mise à jour du numéro de commande dans la base de données

## Besoins non-fonctionnels [5/5]

Bien!

## Bonne utilisation de GitHub et statistiques [4/5]

- Mettre le lien vers le repo GitHub dans le rapport

## Bonus: Application Java [N/A]
