import { cn } from "@/lib/cn";

export function FpIcon({ className = "" }: { className?: string }) {
  return (
    <div
      className={cn(
        "relative inline-flex size-11 shrink-0 items-center justify-center overflow-hidden rounded-[18px]",
        "border border-[rgba(255,255,255,0.7)] text-white",
        "bg-[radial-gradient(circle_at_top_left,rgba(255,255,255,0.4),transparent_38%),linear-gradient(145deg,#6e8d89,#54706c_52%,#b8926a)]",
        "shadow-[0_18px_34px_rgba(78,108,104,0.22)]",
        className,
      )}
      aria-hidden
    >
      <span className="absolute inset-[1px] rounded-[17px] border border-white/12" />
      <span className="block text-center font-mono text-[9px] font-bold leading-tight tracking-[0.08em]">
        01
        <br />
        10
      </span>
    </div>
  );
}
