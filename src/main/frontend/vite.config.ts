import { defineConfig } from 'vite';

export default defineConfig({
    build: {
        manifest: true,
        outDir: '../resources/frontend/',
        emptyOutDir: true,
        rollupOptions: {
            input: './src/main.ts'
        }
    },
    server: {
        host: '127.0.0.1'
    }
});
