import type { Metadata } from "next";
import Link from "next/link";
import { notFound } from "next/navigation";
import { ArrowLeft, ArrowRight, BookOpenText, Sparkles } from "lucide-react";
import { DocHtml } from "@/components/doc-html";
import { Card } from "@/components/ui/card";
import { markdownToHtml } from "@/lib/render-doc";
import { readDocMarkdown } from "@/lib/docs";
import { DOC_SLUGS, getDocNavItem, getDocNeighbors } from "@/lib/nav";

type Props = {
  params: Promise<{ slug?: string[] }>;
};

export function generateStaticParams() {
  return [{ slug: [] }, ...DOC_SLUGS.map((slug) => ({ slug: [slug] }))];
}

export async function generateMetadata({ params }: Props): Promise<Metadata> {
  const { slug } = await params;
  const segments = slug ?? [];
  const key = segments[0] ?? "quick-start";
  const item = getDocNavItem(key);

  return {
    title: item?.title ?? "Documentation",
    description: item?.summary ?? `FastProto guide: ${item?.title ?? "documentation"}.`,
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

  const item = getDocNavItem(key);
  if (!item) notFound();

  const html = await markdownToHtml(markdown);
  const { previous, next } = getDocNeighbors(key);

  return (
    <div className="space-y-6">
      <Card variant="accent" className="paper-grid px-6 py-7 md:px-8 md:py-9">
        <div className="relative z-[1] flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between">
          <div className="max-w-3xl">
            <span className="eyebrow">{item.kicker}</span>
            <h1 className="font-display mt-5 text-4xl tracking-[-0.05em] text-[var(--headline)] md:text-5xl">
              {item.title}
            </h1>
            <p className="mt-4 max-w-2xl text-[15px] leading-7 text-[var(--text-soft)] md:text-base">
              {item.summary}
            </p>
          </div>

          <div className="grid gap-3 text-sm text-[var(--text-soft)] sm:grid-cols-2 lg:max-w-sm">
            <div className="rounded-[22px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.5)] p-4">
              <BookOpenText className="size-4 text-[var(--brand-strong)]" />
              <p className="mt-3 font-semibold text-[var(--headline)]">Readable by design</p>
              <p className="mt-2 leading-6">
                Each guide lives in the same editorial system, so scanning and deep reading feel consistent.
              </p>
            </div>
            <div className="rounded-[22px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.5)] p-4">
              <Sparkles className="size-4 text-[var(--accent)]" />
              <p className="mt-3 font-semibold text-[var(--headline)]">Built for implementation</p>
              <p className="mt-2 leading-6">
                Code samples, API details, and edge-case topics stay close to practical engineering workflows.
              </p>
            </div>
          </div>
        </div>
      </Card>

      <Card className="px-6 py-8 md:px-10 md:py-10">
        <DocHtml html={html} />
      </Card>

      <div className="grid gap-4 md:grid-cols-2">
        {previous ? (
          <Link
            href={`/help/${previous.slug}`}
            className="group rounded-[26px] border border-[var(--line-soft)] bg-[rgba(255,251,246,0.78)] px-5 py-5 shadow-[0_18px_36px_rgba(58,41,23,0.06)]"
          >
            <p className="flex items-center gap-2 text-[11px] font-semibold uppercase tracking-[0.18em] text-[var(--text-dim)]">
              <ArrowLeft className="size-3.5" />
              Previous guide
            </p>
            <p className="font-display mt-3 text-2xl tracking-[-0.03em] text-[var(--headline)]">
              {previous.title}
            </p>
            <p className="mt-2 text-sm leading-6 text-[var(--text-soft)]">{previous.summary}</p>
          </Link>
        ) : (
          <div />
        )}

        {next ? (
          <Link
            href={`/help/${next.slug}`}
            className="group rounded-[26px] border border-[var(--line-soft)] bg-[rgba(255,251,246,0.78)] px-5 py-5 text-left shadow-[0_18px_36px_rgba(58,41,23,0.06)]"
          >
            <p className="flex items-center justify-end gap-2 text-[11px] font-semibold uppercase tracking-[0.18em] text-[var(--text-dim)]">
              Next guide
              <ArrowRight className="size-3.5" />
            </p>
            <p className="font-display mt-3 text-2xl tracking-[-0.03em] text-[var(--headline)] md:text-right">
              {next.title}
            </p>
            <p className="mt-2 text-sm leading-6 text-[var(--text-soft)] md:text-right">{next.summary}</p>
          </Link>
        ) : (
          <div />
        )}
      </div>
    </div>
  );
}
