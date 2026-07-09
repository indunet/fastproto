"use client";

import * as ScrollArea from "@radix-ui/react-scroll-area";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { Compass, MoveUpRight } from "lucide-react";
import { DOC_NAV } from "@/lib/nav";
import { cn } from "@/lib/cn";

export function DocSidebar() {
  const pathname = usePathname();

  return (
    <aside className="min-w-0 lg:sticky lg:top-24 lg:self-start">
      <div className="overflow-hidden rounded-[28px] border border-[var(--line-soft)] bg-[linear-gradient(180deg,rgba(255,251,246,0.82),rgba(249,243,236,0.9))] p-3 shadow-[0_24px_54px_rgba(58,41,23,0.08)] backdrop-blur-sm">
        <div className="flex items-center gap-3 rounded-[22px] border border-white/60 bg-[rgba(255,255,255,0.36)] px-4 py-3">
          <div className="flex size-9 items-center justify-center rounded-2xl bg-[rgba(219,232,229,0.85)] text-[var(--brand-strong)]">
            <Compass className="size-4" />
          </div>
          <div className="min-w-0">
            <p className="text-[0.68rem] font-semibold uppercase tracking-[0.22em] text-[var(--text-dim)]">
              Library Guide
            </p>
            <p className="truncate text-sm font-semibold text-[var(--headline)]">Documentation Map</p>
          </div>
        </div>

        <ScrollArea.Root className="mt-3 h-full max-h-[min(52vh,420px)] w-full lg:max-h-[calc(100vh-9rem)]">
          <ScrollArea.Viewport className="pr-2">
            <nav aria-label="Documentation" className="space-y-1">
              {DOC_NAV.map((item, index) => {
                const href = `/help/${item.slug}`;
                const active = pathname === href;

                return (
                  <Link
                    key={item.slug}
                    href={href}
                    className={cn(
                      "group block rounded-[18px] border px-3.5 py-2.5",
                      active
                        ? "border-[rgba(110,141,137,0.24)] bg-[linear-gradient(135deg,rgba(219,232,229,0.74),rgba(255,251,246,0.95))] shadow-[0_14px_30px_rgba(78,108,104,0.12)]"
                        : "border-transparent bg-transparent hover:border-[var(--line-soft)] hover:bg-[rgba(255,255,255,0.46)]",
                    )}
                  >
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0 flex-1">
                        <div className="flex items-center gap-2">
                          <span className="shrink-0 text-[10px] font-semibold uppercase tracking-[0.18em] text-[var(--text-dim)]">
                            {String(index + 1).padStart(2, "0")}
                          </span>
                          <p className="truncate text-sm font-semibold leading-5 text-[var(--headline)]">
                            {item.title}
                          </p>
                        </div>
                        <p className="mt-1 truncate text-[11px] leading-5 text-[var(--text-soft)]">
                          {item.kicker}
                        </p>
                      </div>
                      <MoveUpRight
                        className={cn(
                          "mt-0.5 size-4 shrink-0 transition-transform",
                          active
                            ? "text-[var(--brand-strong)]"
                            : "text-[var(--text-dim)] group-hover:translate-x-0.5 group-hover:-translate-y-0.5",
                        )}
                      />
                    </div>
                  </Link>
                );
              })}
            </nav>
          </ScrollArea.Viewport>
          <ScrollArea.Scrollbar className="mr-0.5 flex w-1.5 touch-none select-none p-0.5" orientation="vertical">
            <ScrollArea.Thumb className="relative flex-1 rounded-full bg-[rgba(110,141,137,0.3)]" />
          </ScrollArea.Scrollbar>
        </ScrollArea.Root>
      </div>
    </aside>
  );
}
