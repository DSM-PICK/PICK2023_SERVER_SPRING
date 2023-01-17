plugins {
    kotlin("plugin.allopen") version "1.6.21"
}

dependencies {

}

allOpen {
    annotation("com.pickdsm.pickserverspring.common.annotation.UseCase")
    annotation("com.pickdsm.pickserverspring.common.annotation.ReadOnlyUseCase")
}