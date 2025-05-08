FROM python:3.12-slim-bookworm

LABEL maintainer="Jonas Kirschner <kirschner.jonas@gmx.de>"
LABEL description="Dockerfile für den Müller Log-Server mit Pika"

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

WORKDIR /app

RUN chown appuser:appgroup /app # Oder nur chown appuser /app

COPY --chown=appuser:appgroup requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY --chown=appuser:appgroup müller.py .

USER appuser
CMD ["python", "./müller.py"]