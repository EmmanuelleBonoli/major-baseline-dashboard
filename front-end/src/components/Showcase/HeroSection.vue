<template>
  <section class="relative mt-0 min-h-screen flex items-center justify-center z-[2]" id="home">
    <div class="hero-glow absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-4/5 h-4/5 rounded-full"></div>
    <div class="max-w-[1400px] p-8 text-center z-[3] relative">
      <h1 
        data-text="MAJOR BASELINE" 
        class="glitch-text text-[2.5rem] sm:text-[3.5rem] md:text-[5.5rem] mb-4 font-black tracking-[3px] md:tracking-[5px] text-white uppercase relative leading-tight"
      >
        MAJOR BASELINE
      </h1>
      <p class="hero-subtitle text-[1.2rem] md:text-[1.8rem] text-teal mb-8 md:mb-12 tracking-[2px] md:tracking-[3px] uppercase font-bold">/// Développement d'applications & jeux ///</p>
      <div class="flex flex-row flex-wrap gap-4 sm:gap-8 justify-center mt-8 md:mt-12 w-full max-w-[500px] sm:max-w-full mx-auto">
        <a href="#projects" class="btn-glitch teal flex-1 min-w-[140px] max-w-[200px] md:max-w-none relative py-3 md:py-6 px-4 md:px-16 bg-transparent no-underline border-[2px] md:border-[3px] font-black text-[0.85rem] md:text-[1.1rem] tracking-[1px] md:tracking-[3px] uppercase overflow-hidden transition-all duration-300">Nos Projets</a>
        <a href="#studio" class="btn-glitch gold flex-1 min-w-[140px] max-w-[200px] md:max-w-none relative py-3 md:py-6 px-4 md:px-16 bg-transparent no-underline border-[2px] md:border-[3px] font-black text-[0.85rem] md:text-[1.1rem] tracking-[1px] md:tracking-[3px] uppercase overflow-hidden transition-all duration-300">Le Studio</a>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
</script>

<style scoped>
.hero-glow {
  background-image: radial-gradient(circle, color-mix(in srgb, var(--color-teal) 10%, transparent), transparent);
  animation: heroGlitch 5s infinite;
}

.hero-subtitle {
  text-shadow: 0 0 20px color-mix(in srgb, var(--color-teal) 50%, transparent);
}

.btn-glitch {
  clip-path: polygon(10% 0%, 100% 0%, 90% 100%, 0% 100%);
}
.btn-glitch::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  transition: left 0.5s;
}
.btn-glitch:hover::before {
  left: 100%;
}

.btn-glitch.teal {
  border-color: var(--color-teal);
  color: var(--color-teal);
}
.btn-glitch.teal::before {
  background: linear-gradient(90deg, transparent, var(--color-teal), transparent);
}
.btn-glitch.teal:hover {
  color: #fff;
  box-shadow: 0 0 20px var(--color-teal), inset 0 0 20px color-mix(in srgb, var(--color-teal) 50%, transparent);
}

.btn-glitch.gold {
  border-color: var(--color-gold);
  color: var(--color-gold);
}
.btn-glitch.gold::before {
  background: linear-gradient(90deg, transparent, var(--color-gold), transparent);
}
.btn-glitch.gold:hover {
  color: #fff;
  box-shadow: 0 0 20px var(--color-gold), inset 0 0 20px color-mix(in srgb, var(--color-gold) 50%, transparent);
}

@keyframes heroGlitch {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.3; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.5; }
}

.glitch-text::before {
  content: attr(data-text);
  position: absolute;
  top: 0;
  left: 2px;
  width: 100%;
  height: 100%;
  text-shadow: -2px 0 var(--color-teal);
  clip: rect(44px, 9999px, 56px, 0);
  animation: glitch-anim-1 2s infinite linear alternate-reverse;
}

.glitch-text::after {
  content: attr(data-text);
  position: absolute;
  top: 0;
  left: -2px;
  width: 100%;
  height: 100%;
  text-shadow: 2px 0 var(--color-gold);
  clip: rect(44px, 9999px, 56px, 0);
  animation: glitch-anim-2 3s infinite linear alternate-reverse;
}


@keyframes glitch-anim-1 {
  0% { clip: rect(10px, 9999px, 50px, 0); }
  20% { clip: rect(80px, 9999px, 20px, 0); }
  40% { clip: rect(50px, 9999px, 90px, 0); }
  60% { clip: rect(30px, 9999px, 70px, 0); }
  80% { clip: rect(60px, 9999px, 40px, 0); }
  100% { clip: rect(20px, 9999px, 80px, 0); }
}

@keyframes glitch-anim-2 {
  0% { clip: rect(65px, 9999px, 100px, 0); }
  20% { clip: rect(20px, 9999px, 60px, 0); }
  40% { clip: rect(90px, 9999px, 30px, 0); }
  60% { clip: rect(40px, 9999px, 80px, 0); }
  80% { clip: rect(70px, 9999px, 50px, 0); }
  100% { clip: rect(10px, 9999px, 90px, 0); }
}
</style>
