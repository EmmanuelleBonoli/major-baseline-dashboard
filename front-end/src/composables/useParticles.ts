import { onMounted, onUnmounted } from 'vue'

export function useParticles() {
  const particles: HTMLDivElement[] = []
  let styleElement: HTMLStyleElement | null = null

  onMounted(() => {
    const particleCount = 50
    styleElement = document.createElement('style')
    styleElement.id = 'particles-styles'
    let keyframes = ''

    for (let i = 0; i < particleCount; i++) {
      const particle = document.createElement('div')
      particle.className = 'particle'
      particle.style.cssText = `
        position: fixed;
        width: 3px;
        height: 3px;
        background: #1a8b9d;
        border-radius: 50%;
        pointer-events: none;
        z-index: 0;
        box-shadow: 0 0 10px #1a8b9d;
        left: ${Math.random() * 100}%;
        top: ${Math.random() * 100}%;
      `

      keyframes += `
        @keyframes float${i} {
          0% { transform: translate(0,0); opacity: 0; }
          10% { opacity: 1; }
          90% { opacity: 1; }
          100% {
            transform: translate(${Math.random() * 200 - 100}px, ${Math.random() * 200 - 100}px);
            opacity: 0;
          }
        }
      `

      particle.style.animation = `float${i} ${Math.random() * 5 + 3}s infinite`
      document.body.appendChild(particle)
      particles.push(particle)
    }

    styleElement.textContent = keyframes
    document.head.appendChild(styleElement)
  })

  onUnmounted(() => {
    // Supprimer seulement l'élément style des particules
    if (styleElement && styleElement.parentNode) {
      styleElement.parentNode.removeChild(styleElement)
    }
    // Supprimer les particules du DOM
    particles.forEach((p) => {
      if (p.parentNode) {
        p.parentNode.removeChild(p)
      }
    })
  })
}
