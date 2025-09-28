import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import istanbul from 'vite-plugin-istanbul'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
   react(),
    istanbul({
      include: 'src/**/*',
      exclude: ['node_modules', 'cypress'],
      extension: [ '.js', '.jsx', '.ts', '.tsx' ],
      cypress: true,
      requireEnv: false,
    }),
  ],
});
