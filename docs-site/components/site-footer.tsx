import Link from "next/link";

export function SiteFooter() {
  return (
    <footer className="mx-auto mt-10 w-full max-w-7xl px-5 pb-8">
      <div className="rounded-[30px] border border-[var(--line-soft)] bg-[linear-gradient(180deg,rgba(255,251,246,0.86),rgba(248,241,233,0.92))] px-6 py-8 shadow-[0_20px_44px_rgba(58,41,23,0.06)]">
        <div className="flex flex-col gap-6 md:flex-row md:items-end md:justify-between">
          <div className="max-w-xl">
            <p className="text-[0.72rem] font-semibold uppercase tracking-[0.24em] text-[var(--text-dim)]">
              FastProto
            </p>
            <p className="font-display mt-3 text-2xl tracking-[-0.03em] text-[var(--headline)]">
              A calm, precise way to work with binary protocols.
            </p>
            <p className="mt-3 text-sm leading-7 text-[var(--text-soft)]">
              Built for engineers who want protocol definitions to feel readable, durable, and close to the
              spec instead of buried in bitwise boilerplate.
            </p>
          </div>

          <div className="space-y-2 text-sm text-[var(--text-soft)]">
            <p>Released under the Apache 2.0 License.</p>
            <p>
              Source available on{" "}
              <Link className="text-[var(--brand-strong)] underline decoration-[rgba(78,108,104,0.3)] underline-offset-4" href="https://github.com/indunet/fastproto">
                GitHub
              </Link>
              . Maven artifacts live on Maven Central.
            </p>
            <p className="text-xs uppercase tracking-[0.18em] text-[var(--text-dim)]">
              © 2019–2026 indunet.org
            </p>
          </div>
        </div>
      </div>
    </footer>
  );
}
