# FastProto documentation site

Next.js 16 (App Router) + React 19 + TypeScript + Tailwind CSS 4 + Radix UI. Markdown sources live in the parent [`../docs`](../docs) directory.

## Commands

```bash
npm install
npm run dev          # http://localhost:3000
npm run build        # static export → out/ (no basePath)
npm run build:pages  # BASE_PATH=/fastproto for github.io/fastproto/
```

## GitHub Pages

The workflow [`.github/workflows/docs-site.yml`](../.github/workflows/docs-site.yml) runs on **every push to `develop`** (and can be run manually), builds with `BASE_PATH=/fastproto`, and pushes `out/` to the **`gh-pages`** branch.

In the repository **Settings → Pages**: **Source** = **Deploy from a branch**, **Branch** = **`gh-pages`** / **`/(root)`**.
