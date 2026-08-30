<template>
  <LegalPageShell title="Mentions légales" updated-at="30 août 2026">
    <h2>Éditeur du site</h2>
    <p>
      Le présent site « Major Baseline » est édité par <strong>Emmanuelle Bonoli</strong>, personne physique, à titre
      strictement personnel et non professionnel, sans but lucratif.
    </p>
    <p>Contact : via le <RouterLink to="/contact">formulaire de contact</RouterLink> du site.</p>

    <h2>Directeur de la publication</h2>
    <p>Emmanuelle Bonoli.</p>

    <h2>Hébergement</h2>
    <p>
      Le site est hébergé par <strong>OVH SAS</strong><br />
      2 rue Kellermann, 59100 Roubaix, France<br />
      Téléphone : +33 9 72 10 10 07<br />
      <a href="https://www.ovhcloud.com" target="_blank" rel="noopener">www.ovhcloud.com</a>
    </p>

    <h2>Propriété intellectuelle</h2>
    <p>
      Sauf mention contraire, l'ensemble des contenus présents sur ce site (textes, visuels, logos, code source,
      éléments graphiques) est la propriété exclusive de l'éditrice. Toute reproduction, représentation, adaptation ou
      diffusion, totale ou partielle, sans autorisation écrite préalable, est interdite et susceptible de constituer une
      contrefaçon.
    </p>
    <p>
      Les noms, marques et logos des projets présentés (jeux et applications) ainsi que les technologies tierces citées
      demeurent la propriété de leurs titulaires respectifs.
    </p>

    <h2>Responsabilité</h2>
    <p>
      L'éditrice s'efforce d'assurer l'exactitude des informations publiées mais ne saurait être tenue responsable des
      erreurs, omissions ou d'une éventuelle indisponibilité du service. Les liens externes proposés n'engagent pas la
      responsabilité de l'éditrice quant à leur contenu.
    </p>

    <h2>Données personnelles et cookies</h2>
    <p>
      Le traitement des données personnelles et l'utilisation de cookies sont décrits dans la
      <RouterLink to="/confidentialite">politique de confidentialité</RouterLink>. Vous pouvez modifier votre choix
      relatif aux cookies à tout moment via le lien « Gérer les cookies » en pied de page.
    </p>
  </LegalPageShell>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
import LegalPageShell from '@/components/Showcase/LegalPageShell.vue'
import { useSEO } from '@/composables/useSEO'

useSEO({
  title: 'Mentions légales',
  description: 'Mentions légales du site Major Baseline : éditeur, directeur de publication, hébergeur.'
})
</script>
