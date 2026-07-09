import fs from "fs";
import path from "path";
import { DOC_SLUGS } from "./nav";

const DOCS_DIR = path.join(process.cwd(), "..", "docs");

export function readDocMarkdown(slug: string): string | null {
  if (!DOC_SLUGS.includes(slug)) return null;
  const file = path.join(DOCS_DIR, `${slug}.md`);
  if (!fs.existsSync(file)) return null;
  return fs.readFileSync(file, "utf8");
}

export function getPublicBasePath(): string {
  return process.env.BASE_PATH?.trim() || "";
}
