<template>
  <LegalPageShell title="Politique de confidentialité" updated-at="30 août 2026">
    <p>
      La présente politique décrit comment le site « Major Baseline » traite les données personnelles de ses visiteurs,
      conformément au Règlement général sur la protection des données (RGPD) et à la loi « Informatique et Libertés ».
    </p>

    <h2>1. Responsable du traitement</h2>
    <p>
      Emmanuelle Bonoli, éditrice du site à titre personnel.<br />
      Contact : via le <RouterLink to="/contact">formulaire de contact</RouterLink> du site.
    </p>

    <h2>2. Données collectées et finalités</h2>

    <h3>a) Formulaire de contact</h3>
    <ul>
      <li><strong>Données :</strong> adresse e-mail, objet et contenu du message.</li>
      <li><strong>Finalité :</strong> répondre à votre demande.</li>
      <li>
        <strong>Base légale :</strong> votre consentement et l'intérêt légitime de l'éditrice à répondre aux
        sollicitations reçues.
      </li>
      <li>
        <strong>Destinataire :</strong> l'éditrice du site uniquement. Le message est transmis par e-mail via le serveur
        applicatif ; <strong>aucune donnée n'est enregistrée dans une base de données</strong>.
      </li>
      <li>
        <strong>Durée de conservation :</strong> le temps nécessaire au traitement de votre demande, puis suppression
        dans un délai maximal de 2 mois après le dernier échange, sauf obligation légale contraire.
      </li>
    </ul>

    <h3>b) Mesure d'audience (Google Analytics)</h3>
    <ul>
      <li>
        <strong>Données :</strong> pages consultées, date et heure de visite, données techniques (type d'appareil,
        navigateur, système, résolution d'écran), identifiants générés par les cookies de mesure. L'adresse IP est
        utilisée par Google pour dériver une localisation approximative ; elle n'est ni journalisée ni stockée par
        Google Analytics 4.
      </li>
      <li><strong>Finalité :</strong> mesurer la fréquentation du site et comprendre son usage pour l'améliorer.</li>
      <li>
        <strong>Base légale :</strong> votre consentement, recueilli via le bandeau cookies. En cas de refus, aucun
        cookie de mesure n'est déposé et aucune donnée n'est transmise à Google.
      </li>
      <li>
        <strong>Sous-traitant :</strong> Google Ireland Limited (Gordon House, Barrow Street, Dublin 4, Irlande). Des
        transferts vers les États-Unis peuvent avoir lieu, encadrés par les clauses contractuelles types de la
        Commission européenne et le Data Privacy Framework.
      </li>
      <li>
        <strong>Durée de conservation :</strong> cookies valables 13 mois maximum ; données d'audience conservées
        jusqu'à 14 mois par Google.
      </li>
    </ul>

    <h2>3. Cookies et traceurs</h2>
    <p>
      Le site ne dépose aucun cookie soumis à consentement sans votre accord préalable. Seuls les traceurs strictement
      nécessaires au fonctionnement du site sont exemptés.
    </p>
    <div class="table-scroll">
      <table>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Émetteur</th>
            <th>Finalité</th>
            <th>Durée</th>
            <th>Consentement</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><code>_ga</code></td>
            <td>Google Analytics</td>
            <td>Distinguer les visiteurs</td>
            <td>13 mois</td>
            <td>Requis</td>
          </tr>
          <tr>
            <td><code>_ga_&lt;id&gt;</code></td>
            <td>Google Analytics</td>
            <td>Maintenir l'état de session de mesure</td>
            <td>13 mois</td>
            <td>Requis</td>
          </tr>
          <tr>
            <td><code>mb-cookie-consent</code> (stockage local)</td>
            <td>Site</td>
            <td>Mémoriser votre choix relatif aux cookies</td>
            <td>Jusqu'à effacement</td>
            <td>Exempté (nécessaire)</td>
          </tr>
        </tbody>
      </table>
    </div>
    <p>
      L'accès au tableau de bord, réservé à l'éditrice, utilise un jeton d'authentification stocké localement par le
      navigateur. Ce stockage est strictement nécessaire au service et n'est pas utilisé à des fins de suivi.
    </p>
    <p>
      Vous pouvez à tout moment modifier votre choix via le lien « Gérer les cookies » en pied de page, ou en
      configurant votre navigateur pour bloquer les cookies.
    </p>

    <h2>4. Vos droits</h2>
    <p>
      Conformément au RGPD, vous disposez des droits d'accès, de rectification, d'effacement, de limitation,
      d'opposition et de portabilité de vos données. Vous pouvez les exercer via le
      <RouterLink to="/contact">formulaire de contact</RouterLink> du site.
    </p>
    <p>
      Vous avez également le droit d'introduire une réclamation auprès de la Commission nationale de l'informatique et
      des libertés (CNIL) : <a href="https://www.cnil.fr" target="_blank" rel="noopener">www.cnil.fr</a>.
    </p>

    <h2>5. Sécurité</h2>
    <p>
      Les échanges avec le site sont chiffrés via HTTPS. Les données transmises par le formulaire de contact ne
      transitent que vers la messagerie de l'éditrice.
    </p>

    <h2>6. Modifications</h2>
    <p>
      La présente politique peut être mise à jour. Toute modification substantielle donnera lieu à une nouvelle demande
      de consentement pour les cookies de mesure d'audience.
    </p>
  </LegalPageShell>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
import LegalPageShell from '@/components/Showcase/LegalPageShell.vue'
import { useSEO } from '@/composables/useSEO'

useSEO({
  title: 'Politique de confidentialité',
  description:
    'Politique de confidentialité du site Major Baseline : données collectées, cookies, base légale, durées de conservation et exercice de vos droits.'
})
</script>
