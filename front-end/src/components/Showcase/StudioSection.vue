<template>
  <section class="relative py-20 md:py-32 px-4 md:px-8 z-[2]" id="studio">
    <div class="max-w-[1200px] mx-auto text-center">
      <h2 class="section-title text-[2.5rem] md:text-[4rem] mb-4 font-black tracking-[3px] md:tracking-[5px] uppercase text-white">
        Le Studio
      </h2>
      <p class="text-[1rem] md:text-[1.3rem] text-gold mb-12 md:mb-20 tracking-[1px] md:tracking-[2px] uppercase">
        /// Artisanat et simplicité ///
      </p>

      <div class="studio-card grid grid-cols-1 md:grid-cols-2 gap-12 text-left border border-teal/40 p-8 md:p-12 rounded-lg backdrop-blur-lg relative overflow-hidden group">

        <div class="ambient-glow-gold absolute top-0 right-0 w-32 h-32 rounded-full -translate-y-1/2 translate-x-1/2 blur-2xl"></div>
        <div class="ambient-glow-teal absolute bottom-0 left-0 w-48 h-48 rounded-full translate-y-1/2 -translate-x-1/2 blur-2xl"></div>

        <div class="relative z-[3]">
          <h3 class="text-teal text-[1.6rem] md:text-[2rem] font-bold tracking-[2px] mb-6 uppercase flex items-center gap-3">
            <span class="text-gold text-[1.2rem]">/*</span> Ambition
          </h3>
          <p class="text-slate-300 text-[1.1rem] leading-relaxed mb-6">
            <strong class="text-white">Major Baseline</strong> est né de l'envie de créer des applications pratiques et des jeux amusants, sans prétention mais avec passion.
          </p>
          <p class="text-slate-300 text-[1.1rem] leading-relaxed mb-6">
            L'objectif est de concevoir des outils utiles pour le quotidien et des jeux simples pour se divertir, en privilégiant l'expérience utilisateur avant tout.
          </p>
           <p class="text-slate-300 text-[1.1rem] leading-relaxed">
            Ce studio est avant tout un espace d'expérimentation et de partage!
          </p>
        </div>

        <div class="relative z-[3] flex flex-col items-center justify-center border-t md:border-t-0 md:border-l border-teal/20 pt-10 md:pt-0 md:pl-12">
          <div class="w-32 h-32 md:w-40 md:h-40 relative flex items-center justify-center mb-6">

            <div class="absolute inset-0 border-2 border-dashed border-teal/50 rounded-full animate-[spin_10s_linear_infinite]"></div>
            <div class="absolute inset-2 border-2 border-gold/30 rounded-full animate-[spin_15s_linear_infinite_reverse]"></div>
            <div class="avatar-container absolute inset-4 rounded-full bg-black/80 flex items-center justify-center">
              <span class="text-5xl"><img src="/founder.png" alt="Fondatrice"></span>
            </div>
          </div>
          <h3 class="text-white text-[1.5rem] md:text-[1.8rem] font-bold tracking-[2px] mb-2 uppercase text-center">Emmanuelle</h3>
          <p class="text-gold uppercase tracking-[3px] text-sm md:text-md mb-6 font-bold">Fondatrice & Développeuse Full Stack</p>
          <p class="text-slate-400 text-center text-[1rem] leading-relaxed px-4">
            Seule aux commandes, c'est l'envie d'explorer le code et de concevoir des projets variés qui donne vie à Major Baseline.
          </p>
        </div>

      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
</script>

<style scoped>
.section-title {
  text-shadow: 0 0 20px var(--color-teal);
}

.studio-card {
  background-color: color-mix(in srgb, var(--color-deep-blue) 60%, transparent);
  box-shadow: 0 0 30px color-mix(in srgb, var(--color-teal) 15%, transparent);
}

.ambient-glow-gold {
  background-image: radial-gradient(circle, color-mix(in srgb, var(--color-gold) 10%, transparent), transparent);
}

.ambient-glow-teal {
  background-image: radial-gradient(circle, color-mix(in srgb, var(--color-teal) 15%, transparent), transparent);
}

.avatar-container {
  box-shadow: inset 0 0 20px color-mix(in srgb, var(--color-teal) 50%, transparent);
}
</style>
