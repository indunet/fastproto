import Link from "next/link";
import { FpIcon } from "@/components/fp-icon";
import { Button } from "@/components/ui/button";
import { BookOpen, Github, Home, Package, Sparkles } from "lucide-react";

const REPO = "https://github.com/indunet/fastproto";
const MAVEN = "https://search.maven.org/artifact/org.indunet/fastproto/4.1.0/jar";

type SiteHeaderProps = {
  variant?: "marketing" | "docs";
  title?: string;
  subtitle?: string;
};

export function SiteHeader({
  variant = "marketing",
  title = "FastProto",
  subtitle = "Binary protocol craftsmanship for Java systems",
}: SiteHeaderProps) {
  return (
    <header className="sticky top-0 z-50 px-3 pt-3 md:px-5">
      <div className="mx-auto flex max-w-7xl items-center justify-between gap-4 rounded-[26px] border border-[var(--line-soft)] bg-[rgba(255,251,246,0.72)] px-4 py-3 shadow-[0_18px_40px_rgba(58,41,23,0.06)] backdrop-blur-xl md:px-5">
        <Link
          href="/"
          className="group flex min-w-0 items-center gap-3 rounded-full outline-none focus-visible:ring-2 focus-visible:ring-[rgba(110,141,137,0.3)]"
        >
          <FpIcon className="size-10 md:size-11" />
          <div className="min-w-0">
            <p className="truncate text-sm font-semibold tracking-[-0.02em] text-[var(--headline)] md:text-[0.95rem]">
              {title}
            </p>
            <p className="truncate text-[11px] text-[var(--text-soft)] md:text-xs">
              {variant === "docs" ? "Documentation atelier" : subtitle}
            </p>
          </div>
        </Link>

        <div className="hidden items-center gap-2 lg:flex">
          <div className="inline-flex items-center gap-2 rounded-full border border-[var(--line-soft)] bg-[rgba(255,255,255,0.48)] px-3 py-1.5 text-[11px] font-medium text-[var(--text-soft)]">
            <Sparkles className="size-3.5 text-[var(--accent)]" />
            Annotation-first. Runtime-light. Production-ready.
          </div>
        </div>

        <nav className="flex items-center gap-1.5">
          <Button variant="ghost" size="sm" asChild>
            <Link href="/">
              <Home />
              Home
            </Link>
          </Button>
          <Button variant="ghost" size="sm" asChild>
            <Link href="/help/quick-start">
              <BookOpen />
              Docs
            </Link>
          </Button>
          <Button variant="ghost" size="sm" asChild>
            <a href={REPO} target="_blank" rel="noreferrer">
              <Github />
              GitHub
            </a>
          </Button>
          <Button variant="primary" size="sm" asChild>
            <a href={MAVEN} target="_blank" rel="noreferrer">
              <Package />
              Maven
            </a>
          </Button>
        </nav>
      </div>
    </header>
  );
}
