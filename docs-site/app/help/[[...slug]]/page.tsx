import type { Metadata } from "next";
import { notFound } from "next/navigation";
import { DocHtml } from "@/components/doc-html";
import { Card } from "@/components/ui/card";
import { markdownToHtml } from "@/lib/render-doc";
import { readDocMarkdown } from "@/lib/docs";
import { DOC_NAV, DOC_SLUGS } from "@/lib/nav";

type Props = {
  params: Promise<{ slug?: string[] }>;
};

export function generateStaticParams() {
  return [
    { slug: [] },
    ...DOC_SLUGS.map((slug) => ({ slug: [slug] })),
  ];
}

export async function generateMetadata({ params }: Props): Promise<Metadata> {
  const { slug } = await params;
  const segments = slug ?? [];
  const key = segments[0] ?? "quick-start";
  const item = DOC_NAV.find((i) => i.slug === key);
  return {
    title: item?.title ?? "Documentation",
    description: `FastProto guide: ${item?.title ?? "documentation"}.`,
  };
}

export default async function HelpDocPage({ params }: Props) {
  const { slug } = await params;
  const segments = slug ?? [];
  if (segments.length > 1) notFound();
  const key = segments[0] ?? "quick-start";
  if (!DOC_SLUGS.includes(key)) notFound();

  const markdown = readDocMarkdown(key);
  if (!markdown) notFound();

  const html = await markdownToHtml(markdown);

  return (
    <Card className="min-h-[60vh] p-6 md:p-8">
      <DocHtml html={html} />
    </Card>
  );
}
