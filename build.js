const fs = require('fs');
const path = require('path');

console.log('[Build] Starting Cloudflare Pages build for Quran Companion...');

const distDir = path.join(__dirname, 'dist');
const publicDir = path.join(__dirname, 'public');

if (!fs.existsSync(distDir)) {
  fs.mkdirSync(distDir, { recursive: true });
}

// Copy public directory to dist if it exists
if (fs.existsSync(publicDir)) {
  fs.cpSync(publicDir, distDir, { recursive: true });
}

// Ensure index.html exists in dist
const distIndex = path.join(distDir, 'index.html');
const publicIndex = path.join(publicDir, 'index.html');

if (!fs.existsSync(distIndex) && fs.existsSync(publicIndex)) {
  fs.copyFileSync(publicIndex, distIndex);
}

console.log('[Build] Successfully generated deployment output in dist/ and public/');
