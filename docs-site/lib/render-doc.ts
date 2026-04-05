import rehypePrettyCode from "rehype-pretty-code";
import rehypeStringify from "rehype-stringify";
import remarkGfm from "remark-gfm";
import remarkParse from "remark-parse";
import remarkRehype from "remark-rehype";
import { unified } from "unified";
import { visit } from "unist-util-visit";
import type { Element, Root as HastRoot } from "hast";
import { DOC_SLUGS } from "./nav";

function resolveMdHref(href: string): string | null {
  if (!href || /^https?:\/\//i.test(href) || href.startsWith("mailto:")) {
    return null;
  }
  const [pathPart, frag] = href.split("#");
  const clean = pathPart.replace(/^\.\//, "").replace(/^docs\//, "");
  if (!clean.endsWith(".md")) return null;
  const slug = clean.slice(0, -3);
  if (!DOC_SLUGS.includes(slug)) return null;
  const hash = frag ? `#${frag}` : "";
  return `/help/${slug}${hash}`;
}

function rehypeInternalDocLinks() {
  return (tree: HastRoot) => {
    visit(tree, "element", (node: Element) => {
      if (node.tagName !== "a") return;
      const href = node.properties.href;
      if (typeof href !== "string") return;
      const internal = resolveMdHref(href);
      if (internal) node.properties.href = internal;
    });
  };
}

export async function markdownToHtml(markdown: string): Promise<string> {
  const file = await unified()
    .use(remarkParse)
    .use(remarkGfm)
    .use(remarkRehype)
    .use(rehypePrettyCode, {
      theme: "github-light",
      keepBackground: true,
    })
    .use(rehypeInternalDocLinks)
    .use(rehypeStringify)
    .process(markdown);

  return String(file);
}
