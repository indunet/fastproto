import { mkdirSync, writeFileSync } from "fs";
import { dirname, join } from "path";
import { fileURLToPath } from "url";

const __dirname = dirname(fileURLToPath(import.meta.url));
const siteRoot = join(__dirname, "..");
const outPath = join(siteRoot, "public", "assistant-config.json");

const basePath = (process.env.BASE_PATH || "").replace(/\/$/, "");

const guides = [
  ["quick-start", "Quick Start"],
  ["annotation-mapping", "Annotation Mapping"],
  ["byte-and-bit-order", "Byte & Bit Order"],
  ["checksum", "Checksum / CRC"],
  ["expect", "Expect Assertions"],
  ["formulas", "Transformation Formulas"],
  ["arrays-and-strings", "Arrays & Strings"],
  ["variable-length", "Variable Length & Struct Arrays"],
  ["dynamic-offset", "Dynamic Offset (offsetRef)"],
  ["without-annotations", "APIs without Annotations"],
  ["android", "Android"],
  ["netty-integration", "Netty Integration"],
  ["kafka-integration", "Kafka Integration"],
  ["faq", "FAQ"],
].map(([slug, title]) => ({
  id: slug,
  title,
  href: `${basePath}/help/${slug}`,
}));

const payload = {
  version: 1,
  generatedAt: new Date().toISOString(),
  basePath: basePath || "/",
  guides,
};

mkdirSync(dirname(outPath), { recursive: true });
writeFileSync(outPath, JSON.stringify(payload, null, 2), "utf8");
console.log("Wrote assistant-config.json with", guides.length, "guides.");
